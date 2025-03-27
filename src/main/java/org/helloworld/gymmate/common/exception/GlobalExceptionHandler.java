package org.helloworld.gymmate.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorDetails.of(e));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
		// 필드별 에러 메시지 생성
		String errorMessage = ex.getBindingResult().getFieldErrors()
			.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining(", "));

		ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorDetails(errorCode.getHttpStatus(), errorCode.getErrorCode(), errorMessage));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleException(Exception e) {
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(new ErrorDetails(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
				ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
				ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
	}
}
