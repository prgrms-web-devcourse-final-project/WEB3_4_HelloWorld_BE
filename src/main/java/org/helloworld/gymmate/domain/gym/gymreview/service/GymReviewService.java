package org.helloworld.gymmate.domain.gym.gymreview.service;

import java.util.List;

import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewRequest;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReview;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReviewImage;
import org.helloworld.gymmate.domain.gym.gymreview.mapper.GymReviewMapper;
import org.helloworld.gymmate.domain.gym.gymreview.repository.GymReviewRepository;
import org.helloworld.gymmate.domain.gym.gymticket.service.GymTicketService;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
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

    @Transactional
    public Long createGymReview(GymReviewRequest request, List<MultipartFile> images, Long memberId) {
        PartnerGym partnerGym = partnerGymService.getPartnerGymByGymId(request.gymId());
        gymTicketService.hasTicket(partnerGym.getPartnerGymId(), memberId);
        GymReview gymReview = GymReviewMapper.toEntity(request, memberId, partnerGym);
        gymReview = gymReviewRepository.save(gymReview);
        if (images != null && !images.isEmpty()) {
            saveGymReviewImages(gymReview, images);
        }
        return gymReview.getGymReviewId();
    }

    private void saveGymReviewImages(GymReview gymReview, List<MultipartFile> images) {
        List<String> imageUrls = fileManager.uploadFiles(images, "gymReview");
        List<GymReviewImage> gymReviewImages = imageUrls.stream()
            .map(url -> GymReviewMapper.toImageEntity(url, gymReview))
            .toList();
        gymReview.getImages().addAll(gymReviewImages);
    }

}
