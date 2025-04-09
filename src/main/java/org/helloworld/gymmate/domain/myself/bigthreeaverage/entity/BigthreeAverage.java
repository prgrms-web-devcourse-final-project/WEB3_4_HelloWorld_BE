package org.helloworld.gymmate.domain.myself.bigthreeaverage.entity;

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
@Table(name = "bigthree_average")
public class BigthreeAverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bigthree_average_id")
    private Long bigthreeAverageId;

    @Column(name = "sum_average", nullable = false)
    private double sumAverage;

    @Column(name = "bench_average", nullable = false)
    private double benchAverage;

    @Column(name = "deadlift_average", nullable = false)
    private double deadliftAverage;

    @Column(name = "squat_average", nullable = false)
    private double squatAverage;
}
