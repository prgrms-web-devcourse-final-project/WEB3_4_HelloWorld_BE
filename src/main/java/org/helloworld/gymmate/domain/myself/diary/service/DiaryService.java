package org.helloworld.gymmate.domain.myself.diary.service;

import java.time.LocalDate;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryCreateRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryResponse;
import org.helloworld.gymmate.domain.myself.diary.entity.Diary;
import org.helloworld.gymmate.domain.myself.diary.mapper.DiaryMapper;
import org.helloworld.gymmate.domain.myself.diary.repository.DiaryRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {
	private final DiaryRepository diaryRepository;
	private final MemberService memberService;

	@Transactional
	public Long createDiary(DiaryCreateRequest request, Long memberId) {
		Member member = getMember(memberId);

		// 날짜가 비어있으면 현재 날짜 넣기
		LocalDate date = request.date() != null ? request.date() : LocalDate.now();

		return diaryRepository.save(DiaryMapper.toEntity(request, member, date)).getDiaryId();
	}

	@Transactional
	public void deleteDiary(Long diaryId, Long memberId) {
		Member member = getMember(memberId);
		Diary existDiary = getExistingDiary(diaryId);

		validateDiaryOwner(existDiary, member);

		diaryRepository.delete(existDiary);
	}

	@Transactional
	public Long modifyDiary(Long diaryId, DiaryRequest request, Long memberId) {
		Member member = getMember(memberId);
		Diary existingDiary = getExistingDiary(diaryId);

		validateDiaryOwner(existingDiary, member);

		// 기존 날짜 가져오기
		LocalDate date = existingDiary.getDate();

		return diaryRepository.save(DiaryMapper.toEntity(request, member, date)).getDiaryId();
	}

	public Page<DiaryResponse> getDiaries(int page, int size, Long memberId) {
		Member member = getMember(memberId);
		Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

		Page<Diary> diaryPage = diaryRepository.findByMember(member, pageable);

		return diaryPage.map(DiaryMapper::toDto);
	}

	private Member getMember(Long memberId) {
		return memberService.findByUserId(memberId);
	}

	private Diary getExistingDiary(Long diaryId) {
		//기존 운동 기록 가져오기
		return diaryRepository.findById(diaryId)
			.orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));
	}

	private void validateDiaryOwner(Diary diary, Member member) {
		// 본인의 운동 기록이 아니면 예외 발생
		if (!diary.getMember().equals(member)) {
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
	}
}
