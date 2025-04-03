package org.helloworld.gymmate.common.validate.custom;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator extends AbstractImageFileValidator
	implements ConstraintValidator<ValidImageFile, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null)
			return true;

		List<MultipartFile> files = (value instanceof MultipartFile)
			? List.of((MultipartFile)value)
			: (List<MultipartFile>)value;

		for (MultipartFile file : files) {
			String errorMessage = validateFile(file);
			if (errorMessage != null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
				return false;
			}
		}
		return true;
	}
}
