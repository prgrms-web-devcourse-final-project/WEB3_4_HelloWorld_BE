package org.helloworld.gymmate.domain.user.trainer.award.entity;

import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "award")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id", nullable = false)
    private Long awardId;

    @Column(name = "award_year", nullable = false)
    private String awardYear;

    @Column(name = "award_name", nullable = false)
    private String awardName;

    @Column(name = "award_info", nullable = false)
    private String awardInfo;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}
