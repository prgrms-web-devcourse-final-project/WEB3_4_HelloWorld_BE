package org.helloworld.gymmate.domain.pt.reservation.entity;

import java.time.LocalDate;

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
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "trainer_id")
    private Long trainerId;

    // 날짜와 시간을 변경하는 메서드
    public void modifyDateAndTime(LocalDate newDate, Integer newTime) {
        this.date = newDate;
        this.time = newTime;
    }

    public void addCancelDate(LocalDate date) {
        this.cancelDate = date;
    }
}
