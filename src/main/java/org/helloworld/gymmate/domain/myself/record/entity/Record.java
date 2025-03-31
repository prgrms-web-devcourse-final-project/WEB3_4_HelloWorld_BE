package org.helloworld.gymmate.domain.myself.record.entity;

import jakarta.persistence.*;
import lombok.*;
import org.helloworld.gymmate.domain.user.member.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
