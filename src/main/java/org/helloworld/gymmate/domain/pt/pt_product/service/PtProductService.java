package org.helloworld.gymmate.domain.pt.pt_product.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductModifyRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductResponse;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProductImage;
import org.helloworld.gymmate.domain.pt.pt_product.enums.SearchOption;
import org.helloworld.gymmate.domain.pt.pt_product.enums.SortOption;
import org.helloworld.gymmate.domain.pt.pt_product.mapper.PtProductMapper;
import org.helloworld.gymmate.domain.pt.pt_product.repository.PtProductRepository;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PtProductService {
	private final PtProductRepository ptProductRepository;
	private final TrainerRepository trainerRepository;
	private final AwardRepository awardRepository;
	private final GymRepository gymRepository;
	private final FileManager fileManager;

	@Transactional
	public Long createPtProduct(@Valid PtProductCreateRequest request, List<MultipartFile> images) {
		// TODO : 로그인한 id 받아와야 함
		Long trainerId = 0L;

		PtProduct ptProduct = PtProductMapper.toEntity(request, trainerId);
		ptProduct = ptProductRepository.save(ptProduct);
		if (images != null && !images.isEmpty()) {
			saveProductImages(ptProduct, images);
		}
		return ptProduct.getPtProductId();
	}

	@Transactional
	public Long modifyPtProduct(Long productId, @Valid PtProductModifyRequest request, List<MultipartFile> images) {
		// TODO : 로그인한 id 받아와야 함
		Long trainerId = 0L;

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
	public void deletePtProduct(Long productId) {
		// TODO : 로그인한 id 받아와야 함
		Long trainerId = 0L;

		PtProduct ptProduct = findProductOrThrow(productId);
		validateOwnership(ptProduct, trainerId);
		ptProduct.getPtProductImages()
			.forEach(image -> fileManager.deleteFile(image.getUrl()));

		ptProductRepository.delete(ptProduct);
	}

	private void saveProductImages(PtProduct ptProduct, List<MultipartFile> images) {
		List<String> imageUrls = fileManager.uploadFiles(images, "ptProduct");
		List<PtProductImage> ptProductImages = imageUrls.stream()
			.map(url -> PtProductMapper.toEntity(url, ptProduct))
			.toList();

		ptProduct.getPtProductImages().addAll(ptProductImages);
	}

	private PtProduct findProductOrThrow(Long productId) {
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

	public Page<PtProductsResponse> getProducts(String sortOption, String searchOption, String searchTerm, int page,
		int pageSize) {
		SortOption sort = SortOption.from(sortOption);
		SearchOption search = SearchOption.from(searchOption);
		Pageable pageable = PageRequest.of(page, pageSize);

		return switch (sort) {
			case LATEST -> fetchLatestProducts(search, searchTerm, pageable);
			case SCORE -> fetchScoreSortedProducts(search, searchTerm, pageable);
			// TODO : member 추가되면 달아줘야 함
			// case NEAREST -> fetchNearestProducts(search, searchTerm, pageable);
			default -> throw new BusinessException(ErrorCode.UNSUPPORTED_SORT_OPTION);
		};
	}

	private Page<PtProductsResponse> fetchLatestProducts(SearchOption search, String searchTerm, Pageable pageable) {
		Page<PtProduct> ptProducts = switch (search) {
			case NONE -> ptProductRepository.findAllByOrderByPtProductIdDesc(pageable);
			case TRAINER -> ptProductRepository.findByTrainerNameOrderByPtProductIdDesc(searchTerm, pageable);
			case PTPRODUCT -> ptProductRepository.findByPtProductNameOrderByPtProductIdDesc(searchTerm, pageable);
			case DISTRICT -> ptProductRepository.findByGymAddressOrderByPtProductIdDesc(searchTerm, pageable);
		};

		return fetchAndMapProducts(ptProducts, pageable);
	}

	private Page<PtProductsResponse> fetchScoreSortedProducts(SearchOption search, String searchTerm,
		Pageable pageable) {
		Page<PtProduct> ptProducts = switch (search) {
			// 검색어 없이 순수 평점순으로만 ptProduct 조회
			case NONE -> ptProductRepository.findAllOrderByTrainerScoreDesc(pageable);
			// 트레이너명으로 검색된 결과에서 Score 순으로 조회
			case TRAINER -> ptProductRepository.findByTrainerNameOrderByScoreDesc(searchTerm, pageable);
			case PTPRODUCT -> ptProductRepository.findByPtProductNameOrderByScoreDesc(searchTerm, pageable);
			case DISTRICT -> ptProductRepository.findByGymAddressOrderByScoreDesc(searchTerm, pageable);
		};

		return fetchAndMapProducts(ptProducts, pageable);
	}

	// TODO : member에 위치 정보 추가되야 함
	// private Page<PtProductsResponse> fetchNearestProducts(SearchOption search, String searchTerm, Pageable pageable) {
	// 	Page<PtProduct> ptProducts = switch (search) {
	// 		case NONE -> ptProductRepository.findAllOrderByDistanceAsc(pageable);
	// 		case TRAINER -> ptProductRepository.findByTrainerNameOrderByDistanceAsc(searchTerm, pageable);
	// 		case PTPRODUCT -> ptProductRepository.findByPtProductNameOrderByDistanceAsc(searchTerm, pageable);
	// 		case DISTRICT -> ptProductRepository.findByGymAddressOrderByDistanceAsc(searchTerm, pageable);
	// 	};
	//
	// 	return fetchAndMapProducts(ptProducts, pageable);
	// }

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
