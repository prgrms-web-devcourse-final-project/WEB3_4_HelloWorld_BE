package org.helloworld.gymmate.domain.gym.gymreview.service;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewModifyRequest;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewRequest;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewResponse;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReview;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReviewImage;
import org.helloworld.gymmate.domain.gym.gymreview.event.GymReviewDeleteEvent;
import org.helloworld.gymmate.domain.gym.gymreview.event.GymScoreUpdateEvent;
import org.helloworld.gymmate.domain.gym.gymreview.mapper.GymReviewMapper;
import org.helloworld.gymmate.domain.gym.gymreview.repository.GymReviewRepository;
import org.helloworld.gymmate.domain.gym.gymticket.service.GymTicketService;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
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
public class GymReviewService {
    private final PartnerGymService partnerGymService;
    private final GymTicketService gymTicketService;
    private final GymReviewRepository gymReviewRepository;
    private final FileManager fileManager;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createGymReview(GymReviewRequest request, List<MultipartFile> images, Long memberId) {
        PartnerGym partnerGym = partnerGymService.getPartnerGymByGymId(request.gymId());
        gymTicketService.hasTicket(partnerGym.getPartnerGymId(), memberId);
        GymReview gymReview = GymReviewMapper.toEntity(request, memberId, partnerGym);
        gymReview = gymReviewRepository.save(gymReview);
        if (images != null && !images.isEmpty()) {
            saveGymReviewImages(gymReview, images);
        }
        eventPublisher.publishEvent(new GymScoreUpdateEvent(partnerGym.getPartnerGymId()));
        return gymReview.getGymReviewId();
    }

    private void saveGymReviewImages(GymReview gymReview, List<MultipartFile> images) {
        List<String> imageUrls = fileManager.uploadFiles(images, "gymReview");
        List<GymReviewImage> gymReviewImages = imageUrls.stream()
            .map(url -> GymReviewMapper.toImageEntity(url, gymReview))
            .toList();
        gymReview.getImages().addAll(gymReviewImages);
    }

    @Transactional
    public Long modifyGymReview(GymReviewModifyRequest request,
        List<MultipartFile> images, long gymReviewId, Long memberId) {
        GymReview gymReview = findById(gymReviewId);
        checkPermission(gymReview, memberId); // 작성자 확인
        gymReview.update(request);
        deleteImagesIfExists(gymReview, request.deleteImageUrls()); // 삭제요청 들어온 이미지 삭제
        if (images != null && !images.isEmpty()) {
            saveGymReviewImages(gymReview, images);
        }
        eventPublisher.publishEvent(new GymScoreUpdateEvent(gymReview.getPartnerGym().getPartnerGymId()));
        return gymReview.getGymReviewId();
    }

    @Transactional(readOnly = true)
    public Page<GymReviewResponse> getGymReviews(long gymId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "gymReviewId"));
        Page<GymReview> gymReviews = gymReviewRepository.findAll(gymId, pageable);
        return gymReviews.map(gymReview -> {
            Member member = memberRepository.findById(gymReview.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            return GymReviewMapper.toResponse(gymReview, member);
        });
    }

    private void deleteImagesIfExists(GymReview gymReview, List<String> deleteImageUrls) {
        Optional.ofNullable(deleteImageUrls)
            .ifPresent(imageUrls -> imageUrls.forEach(url -> {
                gymReview.removeImageByUrl(url);
                fileManager.deleteFile(url);
            }));
    }

    public GymReview findById(long gymReviewId) {
        return gymReviewRepository.findById(gymReviewId).orElseThrow(
            () -> new BusinessException(ErrorCode.GYM_REVIEW_NOT_FOUND)
        );
    }

    private void checkPermission(GymReview gymReview, long memberId) {
        if (!gymReview.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }

    @Transactional
    public void deleteGymReview(long gymReviewId, Long userId) {
        GymReview gymReview = findById(gymReviewId);
        checkPermission(gymReview, userId);
        gymReviewRepository.delete(gymReview);
        // 트랜잭션 커밋 후 처리될 이벤트 발행
        eventPublisher.publishEvent(new GymReviewDeleteEvent(gymReview.getImages()));
    }

}
