package kr.co.direa.dashboard.controller;

import kr.co.direa.dashboard.dto.DashboardResponseDto;
import kr.co.direa.dashboard.dto.WidgetDto;
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

//    @GetMapping("/{userId}/{dashboardId}")
    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponseDto> getDashboard(@PathVariable UUID userId) {
        DashboardResponseDto dashboardResponseDto = dashboardService.getDashboard(userId);
        return ResponseEntity.ok(dashboardResponseDto);
    }

    @PutMapping("/{userId}/widgets")
    public ResponseEntity<Void> updateWidgets(@PathVariable UUID userId,
            @RequestBody List<WidgetDto> widgets) {
        dashboardService.updateWidgets(userId, widgets);
        return ResponseEntity.ok().build();
    }

}
