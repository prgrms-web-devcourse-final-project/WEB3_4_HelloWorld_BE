package org.helloworld.gymmate.domain.myself.bigthree.entity;

import jakarta.persistence.*;
import lombok.*;
import org.helloworld.gymmate.domain.user.member.entity.Member;

import java.time.LocalDate;

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

    @Column(name = "bench")
    private double bench;

    @Column(name = "deadlift")
    private double deadlift;

    @Column(name = "squat")
    private double squat;

    @Column(name = "date")
    private LocalDate date;

    @Setter
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
