package org.helloworld.gymmate.domain.myself.bigthree.entity;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.user.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "bigthree")
public class Bigthree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bigthree_id")
    private Long bigthreeId;

    @Column(name = "bench", nullable = false)
    private double bench;

    @Column(name = "deadlift", nullable = false)
    private double deadlift;

    @Column(name = "squat", nullable = false)
    private double squat;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Setter
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // ====== Business Logic ======

    public void update(double bench, double deadlift, double squat) {
        this.bench = bench;
        this.deadlift = deadlift;
        this.squat = squat;
    }

}
