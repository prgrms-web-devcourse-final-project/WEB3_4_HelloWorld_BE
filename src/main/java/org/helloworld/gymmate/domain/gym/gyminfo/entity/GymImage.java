package org.helloworld.gymmate.domain.gym.gyminfo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "gym_image")
public class GymImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gym_image_id", nullable = false)
	private Long gymImageId;

	@Column(name = "url", nullable = false)
	private String url; // 이미지 경로

	//제한된 setter 사용
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gym_id")
	@JsonIgnore
	private Gym gym;

}
