package org.helloworld.gymmate.domain.gym.gymTicket.entity;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.gym.gymTicket.enums.GymTicketStatus;
import org.helloworld.gymmate.domain.user.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "gym_ticket")
public class GymTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gym_ticket_id", nullable = false)
	private Long gymTicketId;

	@Column(name = "gym_product_name", nullable = false)
	private String gymProductName;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "pt_product_fee", nullable = false)
	private Integer gymProductFee;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private GymTicketStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "partner_gym_id", nullable = false)
	private Long partnerGymId;

}