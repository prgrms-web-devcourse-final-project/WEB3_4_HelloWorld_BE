package org.helloworld.gymmate.domain.gym.facility.entity;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "facility")
public class Facility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_id")
	private Long facilityId;

	@Column(name = "parking")
	private Boolean parking;

	@Column(name = "shower_room")
	private Boolean showerRoom;

	@Column(name = "in_body")
	private Boolean inBody;

	@Column(name = "locker")
	private Boolean locker;

	@Column(name = "wifi")
	private Boolean wifi;

	@Column(name = "sports_wear")
	private Boolean sportsWear;

	@Column(name = "towel")
	private Boolean towel;

	@Column(name = "sauna")
	private Boolean sauna;

	public void update(FacilityRequest facilityRequest) {
		this.parking = facilityRequest.parking();
		this.showerRoom = facilityRequest.showerRoom();
		this.inBody = facilityRequest.inBody();
		this.locker = facilityRequest.locker();
		this.wifi = facilityRequest.wifi();
		this.sportsWear = facilityRequest.sportsWear();
		this.towel = facilityRequest.towel();
		this.sauna = facilityRequest.sauna();
	}

}
