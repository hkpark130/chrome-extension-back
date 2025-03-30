package kr.co.direa.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class WidgetStyle {
    @Column(name = "is_bordered")
    private Boolean isBordered;
    private String component;
}
