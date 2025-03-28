package org.helloworld.gymmate.domain.gym.gym.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "gym")
public class Gym {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gymId;

	@Column(name = "gym_name", nullable = false)
	private String gymName;

	@Column(name = "start_time", nullable = false)
	private String startTime;

	@Column(name = "end_time", nullable = false)
	private String endTime;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name= "isPartner", nullable = false)
	private Boolean isPartner;

	@Column(name = "address" , nullable = false)
	private String address;

	@Column(name = "x_feild", nullable = false)
	private String xField;

	@Column(name = "y_feild",nullable = false)
	private String yField;

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GymImage> images = new ArrayList<>();

	@Column(name = "avg_score",nullable = false)
	private Double avgScore;

	@Column(name = "intro", nullable = false)
	private String intro;

	public void addImages(List<GymImage> images) {
		this.images.addAll(images);
		images.forEach(img -> img.setGym(this));
	}
}

