package kr.co.direa.dashboard.repository;

import kr.co.direa.dashboard.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WidgetRepository extends JpaRepository<Widget, UUID> {

    List<Widget> findAllByDashboardId(Long dashboardId);
}
