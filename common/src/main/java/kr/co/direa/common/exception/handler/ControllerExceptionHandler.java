package kr.co.direa.common.exception.handler;

import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import kr.co.direa.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(ex.getCustomErrorCode().getStatus())
                .body(ApiResponse.error(ex.getCustomErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error("Unexpected Error: {}", ex.getMessage(), ex);

        return ResponseEntity.internalServerError()
                .body(ApiResponse.error(CustomErrorCode.INTERNAL_SERVER_ERROR));
    }
}
