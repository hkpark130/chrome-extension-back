package kr.co.direa.workspace.dto;

import kr.co.direa.workspace.entity.MeetingEvent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MeetingResponseDto {
    private UUID id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private UUID createdBy;
    private UUID groupId;

    public static MeetingResponseDto fromEntity(MeetingEvent event) {
        return MeetingResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .start(event.getStart())
                .end(event.getEnd())
                .createdBy(event.getCreatedBy())
                .groupId(event.getGroup() != null ? event.getGroup().getId() : null)
                .build();
    }
}
