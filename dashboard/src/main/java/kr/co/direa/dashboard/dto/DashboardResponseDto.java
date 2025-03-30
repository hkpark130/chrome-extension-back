package kr.co.direa.dashboard.dto;

import kr.co.direa.dashboard.entity.Dashboard;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DashboardResponseDto {
    private Long id;
    private UUID userId;
    private List<WidgetDto> widgets; // 위젯 정보 포함

    public static DashboardResponseDto fromEntity(Dashboard dashboard) {
        return DashboardResponseDto.builder()
                .id(dashboard.getId())
                .userId(dashboard.getUserId())
                .widgets(dashboard.getWidgets().stream()
                        .map(WidgetDto::fromEntity)
                        .toList())
                .build();
    }
}
