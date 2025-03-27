package kr.co.direa.dashboard.controller;

import kr.co.direa.common.response.ApiResponse;
import kr.co.direa.dashboard.dto.DashboardRequestDto;
import kr.co.direa.dashboard.dto.DashboardResponseDto;
import kr.co.direa.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@RefreshScope
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponseDto> getOrCreateDashboard(@PathVariable UUID userId) {
        DashboardResponseDto dashboard = dashboardService.getOrCreateDashboard(userId);
        return ResponseEntity.ok(dashboard);
    }

}
