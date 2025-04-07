package org.helloworld.gymmate.domain.gym.gymticket.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymproduct.service.GymProductService;
import org.helloworld.gymmate.domain.gym.gymticket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymticket.dto.MemberGymTicketResponse;
import org.helloworld.gymmate.domain.gym.gymticket.dto.PartnerGymTicketResponse;
import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymticket.mapper.GymTicketMapper;
import org.helloworld.gymmate.domain.gym.gymticket.repository.GymTicketRepository;
import org.helloworld.gymmate.domain.gym.partnergym.dao.PartnerGymNameProjection;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymTicketService {
    private final GymTicketRepository gymTicketRepository;
    private final MemberService memberService;
    private final TrainerService trainerService;
    private final GymProductService gymProductService;
    private final PartnerGymService partnerGymService;

    @Transactional
    public Long createTicket(Long userId, Long gymProductId) {
        // TODO : 캐시로그 남겨야 함
        Member member = memberService.findByUserId(userId);
        GymProduct gymProduct = gymProductService.findByGymProductId(gymProductId);
        isPurchasable(member, gymProduct);
        member.updateCash(member.getCash() - gymProduct.getGymProductFee());
        LocalDate startDate = LocalDate.now();
        GymTicket gymTicket = GymTicketMapper.toEntity(gymProduct, member, startDate);
        gymTicketRepository.save(gymTicket);
        return gymTicket.getGymTicketId();
    }

    @Transactional(readOnly = true)
    public GymTicketPurchaseResponse getPurchaseData(Long userId, Long gymProductId) {
        Member member = memberService.findByUserId(userId);
        GymProduct gymProduct = gymProductService.findByGymProductId(gymProductId);
        return GymTicketMapper.toDto(member.getCash(), gymProduct.getGymProductFee());
    }

    private void isPurchasable(Member member, GymProduct gymProduct) {
        if (member.getCash() < gymProduct.getGymProductFee()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_CASH);
        }
    }

    @Transactional(readOnly = true)
    public Page<MemberGymTicketResponse> getMemberTickets(Long memberId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<GymTicket> tickets = gymTicketRepository.findByMember_MemberId(memberId, pageable);

        Set<Long> partnerGymIds = tickets.stream()
            .map(GymTicket::getPartnerGymId)
            .collect(Collectors.toSet());
        List<PartnerGymNameProjection> gymInfos = partnerGymService.getGymNamesByIds(partnerGymIds);
        Map<Long, String> gymNameMap = gymInfos.stream()
            .collect(Collectors.toMap(
                PartnerGymNameProjection::getPartnerGymId,
                PartnerGymNameProjection::getGymName
            ));

        return tickets.map(ticket -> {
            String gymName = gymNameMap.get(ticket.getPartnerGymId());
            return GymTicketMapper.toMemberGymTicketResponse(ticket, gymName);
        });
    }

    @Transactional(readOnly = true)
    public Page<PartnerGymTicketResponse> getPartnerGymTickets(Long trainerId, int page, int pageSize) {
        Trainer trainer = trainerService.findByUserId(trainerId);
        if (!trainer.getIsOwner()) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        PartnerGym partnerGym = partnerGymService.getPartnerGymByOwnerId(trainerId);
        Pageable pageable = PageRequest.of(page, pageSize);
        return gymTicketRepository.findAllByPartnerGymId(partnerGym.getPartnerGymId(), pageable)
            .map(GymTicketMapper::toPartnerGymTicketResponse);
    }

}
