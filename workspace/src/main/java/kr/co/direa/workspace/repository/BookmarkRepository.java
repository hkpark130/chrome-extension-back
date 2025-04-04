package kr.co.direa.workspace.repository;

import kr.co.direa.workspace.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(UUID userId);

    Optional<Bookmark> findByIdAndUserId(Long id, UUID userId);
}
