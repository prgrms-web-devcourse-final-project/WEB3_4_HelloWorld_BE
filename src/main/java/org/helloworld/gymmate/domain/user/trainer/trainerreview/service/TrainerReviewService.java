package org.helloworld.gymmate.domain.user.trainer.trainerreview.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.pt.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewCreateRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.response.TrainerReviewResponse;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReviewImage;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.event.TrainerReviewDeleteEvent;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.event.TrainerScoreUpdateEvent;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.mapper.TrainerReviewMapper;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.repository.TrainerReviewRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerReviewService {
    private final TrainerReviewRepository trainerReviewRepository;
    private final TrainerService trainerService;
    private final FileManager fileManager;
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createTrainerReview(TrainerReviewCreateRequest reviewCreateRequest, List<MultipartFile> images,
        Long memberId) {
        Trainer trainer = trainerService.findByUserId(reviewCreateRequest.trainerId());

        reservationRepository.find(memberId, reviewCreateRequest.trainerId())
            .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        TrainerReview review = TrainerReviewMapper.toEntity(reviewCreateRequest, trainer, memberId);
        trainerReviewRepository.save(review);
        if (images != null && !images.isEmpty()) {
            saveTrainerReviewImages(review, images);
        }
        eventPublisher.publishEvent(new TrainerScoreUpdateEvent(trainer.getTrainerId()));
        return review.getTrainerReviewId();
    }

    @Transactional
    public Long modifyTrainerReview(TrainerReviewModifyRequest modifyRequest, List<MultipartFile> images,
        Long trainerReviewId, Long memberId) {
        TrainerReview trainerReview = findById(trainerReviewId);
        check(trainerReview, memberId);
        trainerReview.update(modifyRequest);
        deleteImages(trainerReview, modifyRequest.deleteImageUrls());
        if (images != null && !images.isEmpty()) {
            saveTrainerReviewImages(trainerReview, images);
        }
        trainerReviewRepository.save(trainerReview);
        eventPublisher.publishEvent(new TrainerScoreUpdateEvent(trainerReview.getTrainer().getTrainerId()));
        return trainerReview.getTrainerReviewId();
    }

    @Transactional(readOnly = true)
    public Page<TrainerReviewResponse> getTrainerReviewList(int page, int size, Long trainerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "trainerReviewId"));
        Page<TrainerReview> reviewList = trainerReviewRepository.findAllByTrainer_TrainerId(pageable,
            trainerId);

        List<Long> memberIds = reviewList.stream()
            .map(TrainerReview::getMemberId)
            .distinct()
            .toList();

        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
            .collect(Collectors.toMap(Member::getMemberId, Function.identity()));

        return reviewList.map(review -> {
            Member member = memberMap.get(review.getMemberId());
            return TrainerReviewMapper.toResponse(review, member);
        });
    }

    @Transactional
    public void deleteTrainerReview(Long trainerReviewId, Long memberId) {
        TrainerReview trainerReview = findById(trainerReviewId);
        check(trainerReview, memberId);
        trainerReviewRepository.delete(trainerReview);
        // 트랜잭션 커밋 후 처리될 이벤트 발행
        eventPublisher.publishEvent(new TrainerReviewDeleteEvent(trainerReview.getImages()));
    }

    public TrainerReview findById(Long trainerReviewId) {
        return trainerReviewRepository.findById(trainerReviewId)
            .orElseThrow(() -> new BusinessException(ErrorCode.TRAINER_REVIEW_NOT_FOUND));
    }

    private void check(TrainerReview trainerReview, Long memberId) {
        if (!trainerReview.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }

    private void deleteImages(TrainerReview trainerReview, List<String> deleteImageUrls) {
        Optional.ofNullable(deleteImageUrls)
            .ifPresent(imageUrls -> imageUrls.forEach(url -> {
                trainerReview.removeImageUrl(url);
                fileManager.deleteFile(url);
            }));
    }

    private void saveTrainerReviewImages(TrainerReview trainerReview, List<MultipartFile> images) {
        List<String> imageUrls = fileManager.uploadFiles(images, "trainerReview");
        List<TrainerReviewImage> trainerReviewImages = imageUrls.stream()
            .map(url -> TrainerReviewMapper.toImageEntity(url, trainerReview))
            .toList();
        trainerReview.getImages().addAll(trainerReviewImages);
    }

}
