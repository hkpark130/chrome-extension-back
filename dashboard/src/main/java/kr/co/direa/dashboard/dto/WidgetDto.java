package kr.co.direa.dashboard.dto;

import kr.co.direa.dashboard.entity.Widget;
import kr.co.direa.dashboard.entity.WidgetPosition;
import kr.co.direa.dashboard.entity.WidgetStyle;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WidgetDto {
    private UUID id;
//    private Long dashboardId;
    private WidgetPosition position;
    private WidgetStyle style;

    public static WidgetDto fromEntity(Widget widget) {
        return WidgetDto.builder()
                .id(widget.getId())
//                .dashboardId(widget.getDashboard().getId())
                .position(widget.getPosition())
                .style(widget.getStyle())
                .build();
    }
}
