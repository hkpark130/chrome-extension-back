package kr.co.direa.workspace.repository;

import kr.co.direa.workspace.entity.MeetingGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingGroupRepository extends JpaRepository<MeetingGroup, UUID> {

}
