package org.helloworld.gymmate.common.exception;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorDetails.of(e));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleException(Exception e) {
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(new ErrorDetails(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
				ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
				ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> handleException(MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
			.getAllErrors()
			.stream()
			.filter(error -> error instanceof FieldError)
			.map(error -> (FieldError)error)
			.map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
			.sorted(Comparator.comparing((String::toString)))
			.collect(Collectors.joining("\n"));

		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
			.body(new ErrorDetails(ErrorCode.INVALID_PARAMETER.getHttpStatus(),
				ErrorCode.INVALID_PARAMETER.getErrorCode(),
				message));
	}
}
