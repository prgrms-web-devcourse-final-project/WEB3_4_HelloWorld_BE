package org.helloworld.gymmate.domain.user.trainer.trainerreview.service;

import java.time.LocalDateTime;
import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.pt.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewCreateRequest;
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

        Reservation reservation = reservationRepository.findByMemberIdAndTrainerId(memberId,
                reviewCreateRequest.trainerId())
            .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        // 현재 시간이 예약 시간 이후인지 확인
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = reservation.getDate().atTime(reservation.getTime(), 0);

        if (now.isBefore(reservationDateTime)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        // 취소된 예약은 리뷰 작성 불가
        if (reservation.getCancelDate() != null) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        TrainerReview review = TrainerReviewMapper.toEntity(reviewCreateRequest, trainer, memberId);
        trainerReviewRepository.save(review);
        if (images != null && !images.isEmpty()) {
            saveTrainerReviewImages(review, images);
        }

        return review.getTrainerReviewId();
    }

    private void saveTrainerReviewImages(TrainerReview trainerReview, List<MultipartFile> images) {
        List<String> imageUrls = fileManager.uploadFiles(images, "trainerReview");
        List<TrainerReviewImage> trainerReviewImages = imageUrls.stream()
            .map(url -> TrainerReviewMapper.toImageEntity(url, trainerReview))
            .toList();
        trainerReview.getImages().addAll(trainerReviewImages);
    }

}
