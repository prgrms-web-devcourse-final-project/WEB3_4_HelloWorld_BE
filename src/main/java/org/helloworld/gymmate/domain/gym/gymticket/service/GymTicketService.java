package org.helloworld.gymmate.domain.gym.gymticket.service;

import java.time.LocalDate;

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
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
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

	public Page<MemberGymTicketResponse> getOwnTickets(Long memberId, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return gymTicketRepository.findByMember_MemberId(memberId, pageable)
			.map(GymTicketMapper::toMemberGymTicketResponse);
	}

	// TODO : 티켓 취소
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

	// TODO : 티켓 상태 변경시키는 서비스

}
