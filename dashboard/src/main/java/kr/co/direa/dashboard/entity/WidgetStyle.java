package kr.co.direa.dashboard.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class WidgetStyle {
    private boolean isBordered;
    private String component;
}
