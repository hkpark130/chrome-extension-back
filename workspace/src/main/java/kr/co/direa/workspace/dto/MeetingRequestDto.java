package kr.co.direa.workspace.dto;

import kr.co.direa.workspace.entity.MeetingEvent;
import kr.co.direa.workspace.entity.MeetingGroup;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MeetingRequestDto {
    private UUID createdBy;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private Boolean recurring;
    private int repeatWeeks;

    /**
     * 단일 이벤트로 변환할 수 있게 만드는 toEntity()
     * 단, 반복일정이면 toEvents(service에서 처리)로 사용
     */
    public MeetingEvent toEntity(MeetingGroup group) {
        return MeetingEvent.builder()
                .title(this.title)
                .start(this.start)
                .end(this.end)
                .createdBy(this.createdBy)
                .group(group)
                .build();
    }
}
