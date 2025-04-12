package org.helloworld.gymmate.domain.user.trainer.trainerreview.service;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.pt.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewCreateRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReviewImage;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.mapper.TrainerReviewMapper;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.repository.TrainerReviewRepository;
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

        return trainerReview.getTrainerReviewId();
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
