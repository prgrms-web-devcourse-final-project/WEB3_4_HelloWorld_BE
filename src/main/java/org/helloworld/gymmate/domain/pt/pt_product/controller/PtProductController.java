package org.helloworld.gymmate.domain.pt.pt_product.controller;

import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.service.PtProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

		if (images == null || images.isEmpty()) {
			return ResponseEntity.ok(
				Map.of("ptClassId",ptProductService.createPtProduct(request)));
		}
		// 이미지가 있을 경우
		return ResponseEntity.ok(
			Map.of("ptClassId",ptProductService.createPtProduct(request, images)));
	}

}
