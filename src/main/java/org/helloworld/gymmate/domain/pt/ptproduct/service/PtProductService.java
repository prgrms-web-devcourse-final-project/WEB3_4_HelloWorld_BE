package org.helloworld.gymmate.domain.pt.ptproduct.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductModifyRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProductImage;
import org.helloworld.gymmate.domain.pt.ptproduct.enums.PtProductSearchOption;
import org.helloworld.gymmate.domain.pt.ptproduct.enums.PtProductSortOption;
import org.helloworld.gymmate.domain.pt.ptproduct.mapper.PtProductMapper;
import org.helloworld.gymmate.domain.pt.ptproduct.repository.PtProductRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PtProductService {
	private final PtProductRepository ptProductRepository;
	private final TrainerRepository trainerRepository;
	private final AwardRepository awardRepository;
	private final GymRepository gymRepository;
	private final MemberService memberService;
	private final FileManager fileManager;

	@Transactional
	public Long createPtProduct(PtProductCreateRequest request, List<MultipartFile> images, Long trainerId) {
		PtProduct ptProduct = PtProductMapper.toEntity(request, trainerId);
		ptProduct = ptProductRepository.save(ptProduct);
		if (images != null && !images.isEmpty()) {
			saveProductImages(ptProduct, images);
		}
		return ptProduct.getPtProductId();
	}

	@Transactional
	public Long modifyPtProduct(Long productId, PtProductModifyRequest request, List<MultipartFile> images,
		Long trainerId) {
		PtProduct ptProduct = findProductOrThrow(productId);
		validateOwnership(ptProduct, trainerId);
		ptProduct.update(request.info(), request.ptProductFee());
		deleteImagesIfExists(ptProduct, request.deleteImageIds());
		if (images != null && !images.isEmpty()) {
			saveProductImages(ptProduct, images);
		}

		return productId;
	}

	@Transactional
	public void deletePtProduct(Long productId, Long trainerId) {
		PtProduct ptProduct = findProductOrThrow(productId);
		validateOwnership(ptProduct, trainerId);
		ptProduct.getPtProductImages()
			.forEach(image -> fileManager.deleteFile(image.getUrl()));
		ptProductRepository.delete(ptProduct);
	}

	@Transactional
	private void saveProductImages(PtProduct ptProduct, List<MultipartFile> images) {
		List<String> imageUrls = fileManager.uploadFiles(images, "ptProduct");
		List<PtProductImage> ptProductImages = imageUrls.stream()
			.map(url -> PtProductMapper.toEntity(url, ptProduct))
			.toList();
		ptProduct.getPtProductImages().addAll(ptProductImages);
	}

	public PtProduct findProductOrThrow(Long productId) {
		return ptProductRepository.findById(productId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PTPRODUCT_NOT_FOUND));
	}

	private void validateOwnership(PtProduct ptProduct, Long trainerId) {
		if (!ptProduct.getTrainerId().equals(trainerId)) {
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
	}

	private void deleteImagesIfExists(PtProduct ptProduct, List<String> deleteImageIds) {
		Optional.ofNullable(deleteImageIds)
			.ifPresent(imageUrls -> imageUrls.forEach(url -> {
				ptProduct.removeImageByUrl(url);
				fileManager.deleteFile(url);
			}));
	}

	public Page<PtProductsResponse> getProducts(String sortOption, String searchOption, String searchTerm,
		int page, int pageSize, Double x, Double y) {
		PtProductSortOption sort = PtProductSortOption.from(sortOption);
		PtProductSearchOption search = PtProductSearchOption.from(searchOption);
		Pageable pageable = PageRequest.of(page, pageSize);

		return switch (sort) {
			case LATEST -> fetchLatestProducts(search, searchTerm, pageable);
			case SCORE -> fetchScoreSortedProducts(search, searchTerm, pageable);
			case NEARBY -> fetchNearbyProductsUsingXY(search, searchTerm, pageable, x, y);
		};
	}

	private Page<PtProductsResponse> fetchLatestProducts(PtProductSearchOption search, String searchTerm,
		Pageable pageable) {
		Page<PtProduct> ptProducts = switch (search) {
			case NONE -> ptProductRepository.findAllByOrderByPtProductIdDesc(pageable);
			case TRAINER -> ptProductRepository.findByTrainerNameOrderByPtProductIdDesc(searchTerm, pageable);
			case PTPRODUCT -> ptProductRepository.findByPtProductNameOrderByPtProductIdDesc(searchTerm, pageable);
			case DISTRICT -> ptProductRepository.findByGymAddressOrderByPtProductIdDesc(searchTerm, pageable);
		};

		return fetchAndMapProducts(ptProducts, pageable);
	}

	private Page<PtProductsResponse> fetchScoreSortedProducts(PtProductSearchOption search, String searchTerm,
		Pageable pageable) {
		Page<PtProduct> ptProducts = switch (search) {
			case NONE -> ptProductRepository.findAllOrderByTrainerScoreDesc(pageable);
			case TRAINER -> ptProductRepository.findByTrainerNameOrderByScoreDesc(searchTerm, pageable);
			case PTPRODUCT -> ptProductRepository.findByPtProductNameOrderByScoreDesc(searchTerm, pageable);
			case DISTRICT -> ptProductRepository.findByGymAddressOrderByScoreDesc(searchTerm, pageable);
		};

		return fetchAndMapProducts(ptProducts, pageable);
	}

	private Page<PtProductsResponse> fetchNearbyProductsUsingXY(PtProductSearchOption ptProductSearchOption,
		String searchTerm,
		Pageable pageable, Double x, Double y) {
		String searchValue = (ptProductSearchOption == PtProductSearchOption.NONE) ? "" : searchTerm;
		Page<PtProduct> ptProducts = ptProductRepository.findNearestPtProductsWithSearch(x, y,
			ptProductSearchOption.name(),
			searchValue, pageable);
		return fetchAndMapProducts(ptProducts, pageable);
	}

	public Page<PtProductsResponse> fetchNearbyProducts(String searchOption, String searchTerm,
		int page, int pageSize, CustomOAuth2User customOAuth2User) {

		PtProductSearchOption search = PtProductSearchOption.from(searchOption);
		Member member = memberService.findByUserId(customOAuth2User.getUserId());
		Double x = Double.valueOf(member.getXField());
		Double y = Double.valueOf(member.getYField());
		Pageable pageable = PageRequest.of(page, pageSize);

		return fetchNearbyProductsUsingXY(search, searchTerm, pageable, x, y);
	}

	@Transactional(readOnly = true)
	protected Page<PtProductsResponse> fetchAndMapProducts(Page<PtProduct> ptProducts, Pageable pageable) {
		// ptProducts에 해당하는 trainerIds 추출(Set으로 같은 id 중복문제 해결)
		Set<Long> trainerIds = ptProducts.getContent().stream()
			.map(PtProduct::getTrainerId)
			.collect(Collectors.toSet());

		// 트레이너 정보 한번의 쿼리로 받아오기
		Map<Long, PtProductsResponse.PtTrainerResponse> trainerMap = trainerRepository.findByTrainerIdIn(trainerIds)
			.stream()
			.collect(Collectors.toMap(
				Trainer::getTrainerId,
				PtProductMapper::toDto
			));

		// Dto 변환
		List<PtProductsResponse> responseList = ptProducts.getContent().stream()
			.map(ptProduct -> PtProductMapper.toDto(ptProduct, trainerMap))
			.toList();

		return new PageImpl<>(responseList, pageable, ptProducts.getTotalElements());
	}

	@Transactional(readOnly = true)
	public PtProductResponse getProduct(Long ptProductId) {

		PtProduct ptProduct = findProductOrThrow(ptProductId);

		Trainer trainer = trainerRepository.findById(ptProduct.getTrainerId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		List<Award> awards = awardRepository.findByTrainerId(trainer.getTrainerId());

		Gym gym = gymRepository.findWithImagesByGymId(trainer.getGym().getGymId())
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));

		return PtProductMapper.toDto(ptProduct, trainer, awards, gym);
	}
}
