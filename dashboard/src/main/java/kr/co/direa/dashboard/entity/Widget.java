package kr.co.direa.dashboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "widgets")
public class Widget extends BaseTimeEntity{
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", referencedColumnName = "id")
    private Dashboard dashboard;

    @Embedded
    private WidgetPosition position;

    @Embedded
    private WidgetStyle style;

    @Builder
    public Widget(UUID id, Dashboard dashboard, WidgetPosition position, WidgetStyle style) {
        this.id = id;
        this.dashboard = dashboard;
        this.position = position;
        this.style = style;
    }
}
