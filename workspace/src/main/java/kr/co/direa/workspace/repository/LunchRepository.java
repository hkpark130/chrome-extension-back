package kr.co.direa.workspace.repository;

import kr.co.direa.workspace.entity.Lunch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LunchRepository extends JpaRepository<Lunch, Long> {
}
