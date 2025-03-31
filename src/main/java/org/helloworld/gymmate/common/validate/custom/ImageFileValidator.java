package org.helloworld.gymmate.common.validate.custom;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, List<MultipartFile>> {

	private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/gif");
	// TODO : 이미지 사이즈 제한 크기 yml로 관리
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 제한

	@Override
	public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
		if (files == null || files.isEmpty()) {
			return true;
		}

		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("비어 있는 파일은 업로드할 수 없습니다.").addConstraintViolation();
				return false;
			}

			if (!ALLOWED_TYPES.contains(file.getContentType())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("지원되지 않는 이미지 형식입니다. (JPG, PNG, GIF)").addConstraintViolation();
				return false;
			}

			if (file.getSize() > MAX_FILE_SIZE) { // 사실상 스프링 설정에서 걸러짐
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("이미지 파일 크기는 최대 10MB까지 가능합니다.").addConstraintViolation();
				return false;
			}
		}

		return true;
	}
}