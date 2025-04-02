package org.helloworld.gymmate.common.validate.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = SingleImageFileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSingleImageFile {
	String message() default "잘못된 이미지 파일입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}