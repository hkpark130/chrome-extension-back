package kr.co.direa.dashboard.service;

import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import kr.co.direa.dashboard.dto.DashboardResponseDto;
import kr.co.direa.dashboard.dto.WidgetDto;
import kr.co.direa.dashboard.entity.Dashboard;
import kr.co.direa.dashboard.entity.Widget;
import kr.co.direa.dashboard.repository.DashboardRepository;
import kr.co.direa.dashboard.repository.WidgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final WidgetRepository widgetRepository;

    public DashboardResponseDto getDashboard(UUID userId) {
        Dashboard dashboard = dashboardRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // NOTE: dashboard가 없으면 대시보드 생성
                    Dashboard newDashboard = Dashboard.builder().userId(userId).build();
                    return dashboardRepository.save(newDashboard);
                });

        return DashboardResponseDto.fromEntity(dashboard);
    }

    public void updateWidgets(UUID userId, List<WidgetDto> widgetDtos) {
        Dashboard dashboard = dashboardRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(
                        CustomErrorCode.NOT_FOUND_DASHBOARD, "대시보드를 찾을 수 없음 UserID: " + userId
                ));

        Set<UUID> incomingIds = widgetDtos.stream()
                .map(WidgetDto::getId)
                .collect(Collectors.toSet());

        List<Widget> existingWidgets = widgetRepository.findAllByDashboardId(dashboard.getId());

        List<Widget> widgetsToDelete = existingWidgets.stream()
                .filter(widget -> !incomingIds.contains(widget.getId()))
                .toList();

        widgetRepository.deleteAll(widgetsToDelete);

        for (WidgetDto dto : widgetDtos) {
            Widget widget = widgetRepository.findById(dto.getId())
                    .map(existing -> {
                        existing.setPosition(dto.getPosition());
                        existing.setStyle(dto.getStyle());
                        return existing;
                    })
                    .orElseGet(() -> Widget.builder()
                            .id(dto.getId())
                            .dashboard(dashboard)
                            .position(dto.getPosition())
                            .style(dto.getStyle())
                            .build());

            widgetRepository.save(widget);
        }
    }
}