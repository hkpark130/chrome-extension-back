package kr.co.direa.dashboard.service;

import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import kr.co.direa.dashboard.dto.DashboardResponseDto;
import kr.co.direa.dashboard.entity.Dashboard;
import kr.co.direa.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public DashboardResponseDto getOrCreateDashboard(UUID userId) {
        //TODO: 해당 사용자의 대시보드가 없으면 자동으로 만들어줘야함
        Dashboard dashboard = dashboardRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Dashboard newDashboard = Dashboard.builder().userId(userId).build();
                    return dashboardRepository.save(newDashboard);
                });

        return DashboardResponseDto.fromEntity(dashboard);
    }

}