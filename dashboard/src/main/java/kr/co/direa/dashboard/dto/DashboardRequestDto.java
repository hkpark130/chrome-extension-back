package kr.co.direa.dashboard.dto;

import jakarta.validation.constraints.NotNull;
import kr.co.direa.dashboard.entity.Dashboard;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
@Builder
public class DashboardRequestDto {
    private UUID userId;

    public Dashboard toEntity() {
        return Dashboard.builder()
                .userId(this.userId)
                .build();
    }
}
