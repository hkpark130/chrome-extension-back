package kr.co.direa.common.response;

import kr.co.direa.common.exception.code.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private HttpStatus status;
    private String errorCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<Void> error(CustomErrorCode customErrorCode) {
        return new ApiResponse<>(customErrorCode.getStatus(), customErrorCode.getRspCode(), customErrorCode.getRspMsg(), null);
    }
}
