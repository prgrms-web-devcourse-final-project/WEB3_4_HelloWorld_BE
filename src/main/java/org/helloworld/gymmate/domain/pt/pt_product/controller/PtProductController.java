package org.helloworld.gymmate.domain.pt.pt_product.controller;

import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductModifyRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.pt_product.service.PtProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ptProduct")
@RequiredArgsConstructor
public class PtProductController {
	private final PtProductService ptProductService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String,Long>> createPtProduct(
		@RequestPart("ptProductData") @Valid PtProductCreateRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
	){
		// TODO : userDetail 넘겨줘야 함

		return ResponseEntity.ok(
			Map.of("ptClassId",ptProductService.createPtProduct(request, images)));
	}

	@PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String,Long>> modifyPtProduct(
		@PathVariable Long productId,
		@RequestPart("ptProductData") @Valid PtProductModifyRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
	){
		// TODO : userDetail 넘겨줘야 함

		return ResponseEntity.ok(
			Map.of("ptClassId",ptProductService.modifyPtProduct(productId, request, images)));
	}

	@DeleteMapping(value = "/{productId}")
	public ResponseEntity<Map<String,Long>> deletePtProduct(
		@PathVariable Long productId
	){
		// TODO : userDetail 넘겨줘야 함
		ptProductService.deletePtProduct(productId);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<PageDto<PtProductsResponse>> getProducts(
		@RequestParam(defaultValue = "score") String sortOption,
		@RequestParam String searchOption,
		@RequestParam String searchTerm,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int pageSize
	){
		return ResponseEntity.ok(PageMapper.toPageDto(
			ptProductService.getProducts(sortOption, searchOption, searchTerm, page, pageSize)));
	}

}
