package org.helloworld.gymmate.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "유저를 찾을 수 없습니다."),
	USER_LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "USER-002", "로그인이 필요합니다."),
	USER_NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "USER-003", "닉네임이 중복 되었습니다."),
	TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TOKEN-001", "토큰이 유효하지 않습니다."),
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW-001", "리뷰를 찾을 수 없습니다."),
	REVIEW_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "REVIEW-002", "리뷰에 대한 삭제/수정 권한이 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL-001", "서버 내부 오류"),
	PAYLOAD_TOO_LARGE(HttpStatus.BAD_REQUEST, "PAYLOAD-001", "파일 크기가 제한을 초과했습니다. (최대 10MB)"),
	FILE_UPLOAD_TYPE_ERROR(HttpStatus.BAD_REQUEST, "FILE-001", "이미지파일 이외의 업로드는 불가능합니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "파일 업로드 중 오류가 발생했습니다."),
	FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-003", "파일 삭제 중 오류가 발생했습니다."),
	DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-004", "디렉토리 생성 중 오류가 발생했습니다."),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "PARAMETER-001", "요청 파라미터가 유효하지 않습니다."),
	INVALID_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "BUSINESS-001", "사업자 등록번호가 유효하지 않습니다."),

	// PT
	CLASSTIME_DUPLICATED(HttpStatus.BAD_REQUEST, "PT-CLASSTIME-001", "이미 등록된 수업 시간입니다."),
	CLASSTIME_NOT_FOUND(HttpStatus.NOT_FOUND, "PT-CLASSTIME-002", "존재하지 않는 PT 수강 가능 시간입니다."),
	PTPRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PT-PRODUCT-001", "존재하지 않는 PT 수업입니다."),
	UNSUPPORTED_SEARCH_OPTION(HttpStatus.BAD_REQUEST, "PT-PRODUCT-002", "존재하지 않는 검색 옵션입니다."),
	UNSUPPORTED_SORT_OPTION(HttpStatus.BAD_REQUEST, "PT-PRODUCT-003", "존재하지 않는 정렬 조건입니다."),

	// Gym
	GYM_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "GYM-001", "잘못된 입력값입니다."),
	GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "GYM-002", "요청한 헬스장이 존재하지 않습니다."),
	GYM_ALREADY_EXISTS(HttpStatus.CONFLICT, "GYM-003", "이미 등록된 헬스장입니다."),
	GYM_REGISTRATION_FORBIDDEN(HttpStatus.FORBIDDEN, "GYM-004", "헬스장 등록 권한이 없습니다."),
	GYM_FORBIDDEN(HttpStatus.FORBIDDEN, "GYM-005", "해당 헬스장에 대한 수정 권한이 없습니다."),

	// PartnerGym
	PARTNER_GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTNER-GYM-001", "파트너 헬스장이 등록되어 있지 않습니다."),

	// Myself
	BIGTHREE_NOT_FOUND(HttpStatus.NOT_FOUND, "MYSELF-BIGTHREE-001", "해당 3대 측정 기록을 찾을 수 없습니다."),
	DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "MYSELF-RECORD-001", "해당 운동 기록을 찾을 수 없습니다."),

	// 인증 관련
	AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH-001", "인증이 필요합니다. 로그인 후 다시 시도해주세요."),
	USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "AUTH-002", "권한이 부족합니다."),

	// 이미지 관련
	IMAGE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "IMAGE-002", "업로드한 파일 크기가 너무 큽니다. 최대 5MB까지 가능합니다."),
	IMAGE_UNSUPPORTED_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "IMAGE-003", "지원되지 않는 파일 형식입니다. (jpg, png, gif만 가능)"),

	// S3 관련
	S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3-001", "이미지 업로드 중 오류가 발생했습니다. 다시 시도해주세요."),

	// API 관련
	API_UNEXPECTED_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "API-001", "API 응답이 올바르지 않습니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}
}

