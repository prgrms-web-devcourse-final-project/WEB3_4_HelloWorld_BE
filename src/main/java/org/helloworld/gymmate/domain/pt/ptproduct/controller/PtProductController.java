package org.helloworld.gymmate.domain.pt.ptproduct.controller;

import java.util.List;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductModifyRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.service.PtProductService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "PT 상품 API", description = "PT 상품 등록,수정,삭제 / 상품 전체조회, 단일조회")
@RestController
@RequestMapping("/ptProduct")
@RequiredArgsConstructor
@Slf4j
public class PtProductController {
    private final PtProductService ptProductService;

    @Operation(summary = "트레이너 - PT 상품 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> createPtProduct(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("ptProductData") @Valid PtProductCreateRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ptProductService.createPtProduct(request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "트레이너 - PT 상품 수정")
    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> modifyPtProduct(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long productId,
        @RequestPart("ptProductData") @Valid PtProductModifyRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ptProductService.modifyPtProduct(productId, request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "트레이너 - PT 수업 삭제")
    @DeleteMapping(value = "/{productId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deletePtProduct(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long productId
    ) {
        ptProductService.deletePtProduct(productId, customOAuth2User.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "PT 상품 전체조회")
    @GetMapping
    @Validated
    public ResponseEntity<PageDto<PtProductsResponse>> getProducts(
        @RequestParam(defaultValue = "score") String sortOption,
        @RequestParam(required = false) String searchOption,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize,
        @RequestParam(required = false, defaultValue = "127.0276") Double x,
        @RequestParam(required = false, defaultValue = "37.4979") Double y
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(
            ptProductService.getProducts(sortOption, searchOption, searchTerm, page, pageSize, x, y)));
    }

    @Operation(summary = "멤버 - 가까운 순 PT 상품 전체조회")
    @GetMapping("/nearby")
    @Validated
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<PageDto<PtProductsResponse>> getNearByProducts(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(required = false) String searchOption,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(
            ptProductService.fetchNearbyProducts(searchOption, searchTerm, page, pageSize, customOAuth2User)));
    }

    @Operation(summary = "단일 PT 상품 조회")
    @GetMapping("/{ptProductId}")
    public ResponseEntity<PtProductResponse> getProduct(
        @PathVariable Long ptProductId
    ) {
        return ResponseEntity.ok(ptProductService.getProduct(ptProductId));
    }
}
