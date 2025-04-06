package org.helloworld.gymmate.domain.gym.gymticket.mapper;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymticket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymticket.enums.GymTicketStatus;
import org.helloworld.gymmate.domain.user.member.entity.Member;

public class GymTicketMapper {
	public static GymTicket toEntity(GymProduct gymProduct, Member member, LocalDate startDate) {
		return GymTicket.builder()
			.gymProductName(gymProduct.getGymProductName())
			.startDate(startDate)
			.endDate(startDate.plusMonths(gymProduct.getGymProductMonth()))
			.gymProductFee(gymProduct.getGymProductFee())
			.status(GymTicketStatus.ACTIVE)
			.member(member)
			.partnerGymId(gymProduct.getPartnerGym().getPartnerGymId())
			.build();
	}

	public static GymTicketPurchaseResponse toDto(long memberCash, long gymProductFee) {
		return new GymTicketPurchaseResponse(
			memberCash,
			gymProductFee,
			memberCash - gymProductFee,
			memberCash >= gymProductFee
		);
	}
}
