package kr.co.direa.dashboard.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class WidgetPosition {
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean isBordered;
    private String component;
}
