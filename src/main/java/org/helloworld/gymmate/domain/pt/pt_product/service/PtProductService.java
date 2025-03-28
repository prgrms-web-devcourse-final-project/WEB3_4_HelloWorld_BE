package org.helloworld.gymmate.domain.pt.pt_product.service;

import java.util.List;

import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProductImage;
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
		PtProduct ptProduct = PtProduct.builder()
			.info(request.info())
			.ptProductFee(request.ptProductFee())
			.trainerId(trainerId)
			.build();

		ptProduct = ptProductRepository.save(ptProduct);
		List<String> imageUrls = fileManager.uploadFiles(images, "ptProduct");

		PtProduct finalPtProduct = ptProduct;
		List<PtProductImage> ptProductImages = imageUrls.stream()
			.map(url -> PtProductImage.builder()
				.url(url)
				.ptProduct(finalPtProduct)
				.build())
			.toList();
		ptProduct.getPtProductImages().addAll(ptProductImages);

		return finalPtProduct.getPtProductId();
	}

	@Transactional
	public Long createPtProduct(@Valid PtProductCreateRequest request) {
		// TODO : 로그인한 id 받아와야 함
		Long trainerId = 0L;
		PtProduct ptProduct = PtProduct.builder()
			.info(request.info())
			.ptProductFee(request.ptProductFee())
			.trainerId(trainerId)
			.build();

		ptProduct = ptProductRepository.save(ptProduct);

		return ptProduct.getPtProductId();
	}
}
