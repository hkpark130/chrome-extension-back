package kr.co.direa.common.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {

//    NOT_FOUND_USER(404, "U001", "해당 유저가 없습니다."),

    NOT_FOUND_BOOKMARK(404, "B001", "해당 북마크가 없습니다."),

    NOT_FOUND_DASHBOARD(404, "D001", "해당 대시보드가 없습니다."),

//    SCHEDULER_ERROR(500, "S001", "scheduler 에러"),
//    MAIL_SEND_ERROR(500, "S002", "메일 송신중 에러"),
    INTERNAL_SERVER_ERROR(500, "D099", "서버 에러");

    private final int status;
    private final String rspCode;
    private final String rspMsg;

    CustomErrorCode(int status, final String rspCode, final String rspMsg) {
        this.status = status;
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }
}
