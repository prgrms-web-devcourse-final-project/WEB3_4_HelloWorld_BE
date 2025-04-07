package org.helloworld.gymmate.domain.gym.gymticket.service;

import java.time.LocalDate;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymproduct.service.GymProductService;
import org.helloworld.gymmate.domain.gym.gymticket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymticket.enums.GymTicketStatus;
import org.helloworld.gymmate.domain.gym.gymticket.mapper.GymTicketMapper;
import org.helloworld.gymmate.domain.gym.gymticket.repository.GymTicketRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymTicketService {
	private final GymTicketRepository gymTicketRepository;
	private final MemberService memberService;
	private final GymProductService gymProductService;

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

	@Transactional
	public void cancelTicket(Long userId, Long gymTicketId) {
		Member member = memberService.findByUserId(userId);
		GymTicket gymTicket = findByGymTicketId(gymTicketId);
		if (!gymTicket.getMember().equals(member)) {
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
		// TODO : 환불정책 고려
		gymTicket.updateStatus(GymTicketStatus.CANCELED);
	}

	public GymTicket findByGymTicketId(Long gymTicketId) {
		return gymTicketRepository.findById(gymTicketId)
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_TICKET_NOT_FOUND));
	}

}
