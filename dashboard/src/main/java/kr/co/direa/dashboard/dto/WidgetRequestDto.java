package kr.co.direa.dashboard.dto;

import kr.co.direa.dashboard.entity.Dashboard;
import kr.co.direa.dashboard.entity.Widget;
import kr.co.direa.dashboard.entity.WidgetPosition;
import kr.co.direa.dashboard.entity.WidgetStyle;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WidgetRequestDto {
    private Long dashboardId;
    private WidgetPosition position;
    private WidgetStyle style;

    public Widget toEntity(Dashboard dashboard) {
        return Widget.builder()
                .dashboard(dashboard)
                .position(this.position)
                .style(this.style)
                .build();
    }
}
