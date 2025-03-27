package kr.co.direa.dashboard.dto;

import kr.co.direa.dashboard.entity.Widget;
import kr.co.direa.dashboard.entity.WidgetPosition;
import kr.co.direa.dashboard.entity.WidgetStyle;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WidgetResponseDto {
    private Long id;
    private WidgetPosition position;
    private WidgetStyle style;

    public static WidgetResponseDto fromEntity(Widget widget) {
        return WidgetResponseDto.builder()
                .id(widget.getId())
                .position(widget.getPosition())
                .style(widget.getStyle())
                .build();
    }
}
