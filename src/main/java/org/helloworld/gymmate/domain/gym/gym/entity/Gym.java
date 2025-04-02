package org.helloworld.gymmate.domain.gym.gym.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.locationtech.jts.geom.Point;

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

	@Column(name = "isPartner", nullable = false)
	private Boolean isPartner;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "location", columnDefinition = "POINT SRID 4326", nullable = false)
	private Point location;

	@Column(name = "avg_score", nullable = false)
	private Double avgScore;

	@Column(name = "intro", nullable = false)
	private String intro;

	@Column(name = "place_url")
	private String placeUrl;

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<GymImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Machine> machines = new ArrayList<>();

	// ====== Business Logic ======

	public void addImages(List<GymImage> images) {
		this.images.addAll(images);
		images.forEach(img -> img.setGym(this));
	}

	public void removeImage(GymImage image) {
		images.remove(image);
		image.setGym(null);
	}

	public void updateInfo(
		String gymName,
		String startTime,
		String endTime,
		String phoneNumber,
		String address,
		String intro
	) {
		this.gymName = gymName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.intro = intro;
	}
}

