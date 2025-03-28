package org.helloworld.gymmate.domain.pt.pt_product.service;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductModifyRequest;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProductImage;
import org.helloworld.gymmate.domain.pt.pt_product.mapper.PtProductMapper;
import org.helloworld.gymmate.domain.pt.pt_product.repository.PtProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PtProductService {
	private final PtProductRepository ptProductRepository;
	private final FileManager fileManager;

	@Transactional
	public Long createPtProduct(@Valid PtProductCreateRequest request, List<MultipartFile> images) {
		// TODO : 로그인한 id 받아와야 함
		Long trainerId = 0L;

		PtProduct ptProduct = PtProductMapper.toEntity(request,trainerId);
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
		ptProduct.update(request.info(),request.ptProductFee());
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
			.map(url -> PtProductMapper.toEntity(url,ptProduct))
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
}
