package org.helloworld.gymmate.domain.gym.gymTicket.service;

import java.time.LocalDate;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymProduct.service.GymProductService;
import org.helloworld.gymmate.domain.gym.gymTicket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymTicket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymTicket.mapper.GymTicketMapper;
import org.helloworld.gymmate.domain.gym.gymTicket.repository.GymTicketRepository;
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

	// TODO : 티켓 상태 변경시키는 서비스

}
