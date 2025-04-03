package org.helloworld.gymmate.domain.gym.gymInfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "Partner_gym")
public class PartnerGym {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "partner_gym_id")
	private Long partnerGymId;

	@Column(name = "owner_id", unique = true, nullable = false)
	private Long ownerId; //헬스장 주인id

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gym_id", unique = true, nullable = false)
	private Gym gym;
}
