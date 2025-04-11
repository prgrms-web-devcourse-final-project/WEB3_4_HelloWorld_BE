package org.helloworld.gymmate.common.exception;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDetails> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(ErrorCode.PAYLOAD_TOO_LARGE.getHttpStatus())
            .body(new ErrorDetails(ErrorCode.PAYLOAD_TOO_LARGE.getHttpStatus(),
                ErrorCode.PAYLOAD_TOO_LARGE.getErrorCode(),
                ErrorCode.PAYLOAD_TOO_LARGE.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(HandlerMethodValidationException ex) {
        String message = ex.getAllValidationResults().stream()
            .flatMap(vr -> vr.getResolvableErrors().stream())
            .map(MessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));

        return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
            .body(new ErrorDetails(ErrorCode.INVALID_PARAMETER.getHttpStatus(),
                ErrorCode.INVALID_PARAMETER.getErrorCode(),
                message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = e.getMessage();

        return buildErrorResponse(ErrorCode.CONSTRAINT_VIOLATION, "DB 제약조건 위반: " + extractConstraintInfo(message));
    }

    // 공통 응답 생성 메서드
    private ResponseEntity<ErrorDetails> buildErrorResponse(ErrorCode code, String customMessage) {
        return ResponseEntity.status(code.getHttpStatus())
            .body(new ErrorDetails(code.getHttpStatus(), code.getErrorCode(), customMessage));
    }

    // 메시지에서 간단한 제약조건 정보 추출
    private String extractConstraintInfo(String dbMessage) {
        // 예: Duplicate entry '115' for key 'partner_gym.gym_id'
        int idx = dbMessage.indexOf("for key");
        return idx != -1 ? dbMessage.substring(idx) : "알 수 없는 제약조건 위반";
    }

}
