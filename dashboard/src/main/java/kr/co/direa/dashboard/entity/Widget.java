package kr.co.direa.dashboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "widgets")
public class Widget extends BaseTimeEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dashboard_id", referencedColumnName = "id")
    private Dashboard dashboard;

    @Embedded
    private WidgetPosition position;

    @Embedded
    private WidgetStyle style;

    @Builder
    public Widget(Dashboard dashboard, WidgetPosition position, WidgetStyle style) {
        this.dashboard = dashboard;
        this.position = position;
        this.style = style;
    }
}
