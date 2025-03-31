package kr.co.direa.workspace.repository;

import kr.co.direa.workspace.entity.MeetingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface MeetingEventRepository extends JpaRepository<MeetingEvent, UUID> {

    List<MeetingEvent> findByGroupId(UUID groupId);

    List<MeetingEvent> findByStartBetween(LocalDateTime start, LocalDateTime end);
}
