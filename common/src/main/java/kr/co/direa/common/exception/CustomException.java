package kr.co.direa.common.exception;

import kr.co.direa.common.exception.code.CustomErrorCode;

public class CustomException extends RuntimeException {
    private CustomErrorCode customErrorCode;

    public CustomException() {
    }

    public CustomException(Exception e) {
        super(e);
    }

    public CustomException(CustomErrorCode customErrorCode, String extMessage) {
        super(customErrorCode.getRspMsg() + " 상세내용[" + extMessage + "]");
        this.customErrorCode = customErrorCode;
    }

    public CustomErrorCode getErrorCode() {
        return customErrorCode;
    }
}
