package org.helloworld.gymmate.domain.gym.gymTicket.service;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymTicket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymTicket.mapper.GymTicketMapper;
import org.helloworld.gymmate.domain.gym.gymTicket.repository.GymTicketRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymTicketService {
	private final GymTicketRepository gymTicketRepository;

	@Transactional
	public GymTicket makeTicket(GymProduct gymProduct, Member member, LocalDate startDate) {
		return GymTicketMapper.toEntity(gymProduct, member, startDate);
	}

	// TODO : 헬스장 Product 구매시.. 이건 GymProduct? GymTicket?

	// TODO : 티켓 상태 변경시키는 서비스

}
