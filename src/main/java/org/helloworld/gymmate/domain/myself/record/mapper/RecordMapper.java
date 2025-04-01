package org.helloworld.gymmate.domain.myself.record.mapper;

import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.myself.record.entity.Record;
import org.helloworld.gymmate.domain.user.member.entity.Member;

import java.time.LocalDate;

public class RecordMapper {
    public static Record toEntity(RecordCreateRequest request, Member member, LocalDate date) {
        return toEntity(request.recordRequest(), member, date);
    }

    public static Record toEntity(RecordRequest request, Member member, LocalDate date) {
        return Record.builder()
                .date(date)
                .title(request.title())
                .content(request.content())
                .member(member)
                .build();
    }

    public static RecordResponse toDto(Record record) {
        return new RecordResponse(
                record.getRecordId(),
                record.getDate(),
                record.getTitle(),
                record.getContent()
        );
    }
}
