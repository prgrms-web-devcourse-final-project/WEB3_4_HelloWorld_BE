package org.helloworld.gymmate.domain.gym.gymInfo.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@With
@Table(name = "gym")
public class Gym {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gymId;

	@Column(name = "gym_name", nullable = false)
	private String gymName;

	@Column(name = "start_time", nullable = false)  //크롤링 해서 없는 경우 "운영시간이 없습니다" 표시
	private String startTime;

	@Column(name = "end_time", nullable = false) //크롤링 해서 없는 경우 "운영시간이 없습니다" 표시
	private String endTime;

	@Column(name = "phone_number", nullable = true) //헬스장 번호가 없는 경우 존재함
	private String phoneNumber;

	@Column(name = "is_partner", nullable = false)
	private Boolean isPartner;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "location", columnDefinition = "POINT SRID 4326", nullable = false)
	private Point location;

	@Column(name = "avg_score", nullable = false)
	private Double avgScore;

	@Column(name = "intro", nullable = false)
	private String intro;

	@Column(name = "place_url") //헬스장 상세 정보URL
	private String placeUrl;

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<GymImage> images = new ArrayList<>();  //이미지의 경우 default이미지 표시

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Machine> machines = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id")
	private Facility facility;

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
		String startTime,
		String endTime,
		String intro
	) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.intro = intro;
	}

	public void assignFacility(Facility facility) {
		this.facility = facility;
	}
}

