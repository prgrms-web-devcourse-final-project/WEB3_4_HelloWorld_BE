package org.helloworld.gymmate.domain.gym.gymTicket.mapper;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymTicket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymTicket.enums.GymTicketStatus;
import org.helloworld.gymmate.domain.user.member.entity.Member;

public class GymTicketMapper {
	public static GymTicket toEntity(GymProduct gymProduct, Member member, LocalDate startDate) {
		return GymTicket.builder()
			.gymProductName("gymProduct.") // ??? gymProduct엔 이름이 없음
			.startDate(startDate)
			.endDate(startDate.plusMonths(gymProduct.getGymProductMonth()))
			.gymProductFee(gymProduct.getGymProductFee())
			.status(GymTicketStatus.ACTIVE)
			.member(member)
			.partnerGymId(gymProduct.getPartnerGym().getPartnerGymId())
			.build();
	}
}
