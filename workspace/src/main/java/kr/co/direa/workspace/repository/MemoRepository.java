package kr.co.direa.workspace.repository;

import kr.co.direa.workspace.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByUserId(UUID userId);
}
