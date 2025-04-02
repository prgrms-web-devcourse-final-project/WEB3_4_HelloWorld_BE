package org.helloworld.gymmate.common.validate.custom;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SingleImageFileValidator implements ConstraintValidator<ValidSingleImageFile, MultipartFile> {

	private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/gif");
	// TODO : 이미지 사이즈 제한 크기 yml로 관리
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 제한

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null || file.isEmpty()) {
			return true; // 파일이 필수가 아닐 경우 허용
		}

		if (!ALLOWED_TYPES.contains(file.getContentType())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("지원되지 않는 이미지 형식입니다. (JPG, PNG, GIF)").addConstraintViolation();
			return false;
		}

		if (file.getSize() > MAX_FILE_SIZE) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("이미지 파일 크기는 최대 10MB까지 가능합니다.").addConstraintViolation();
			return false;
		}

		return true;
	}
}