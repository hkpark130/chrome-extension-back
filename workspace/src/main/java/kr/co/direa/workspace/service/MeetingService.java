package kr.co.direa.workspace.service;

import jakarta.transaction.Transactional;
import kr.co.direa.workspace.dto.MeetingRequestDto;
import kr.co.direa.workspace.dto.MeetingResponseDto;
import kr.co.direa.workspace.entity.MeetingEvent;
import kr.co.direa.workspace.entity.MeetingGroup;
import kr.co.direa.workspace.repository.MeetingEventRepository;
import kr.co.direa.workspace.repository.MeetingGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingEventRepository eventRepository;
    private final MeetingGroupRepository groupRepository;

    /**
     * 회의 저장 (단건 or 반복 포함)
     */
    @Transactional
    public List<MeetingResponseDto> createMeeting(MeetingRequestDto request) {
        List<MeetingEvent> events = new ArrayList<>();
        MeetingGroup group = null;

        // 반복 예약일 경우 그룹 정보 먼저 저장
        if (request.getRecurring()) {
            group = MeetingGroup.builder()
                    .id(UUID.randomUUID())
                    .repeatWeeks(request.getRepeatWeeks())
                    .recurrenceType("WEEKLY") // 현재는 고정값
                    .build();
            groupRepository.save(group);
        }

        int repeatCount = request.getRecurring() ? request.getRepeatWeeks() : 1;

        for (int i = 0; i < repeatCount; i++) {
            LocalDateTime shiftedStart = request.getStart().plusWeeks(i);
            LocalDateTime shiftedEnd = request.getEnd().plusWeeks(i);

            MeetingEvent event = MeetingEvent.builder()
                    .title(request.getTitle())
                    .start(shiftedStart)
                    .end(shiftedEnd)
                    .createdBy(request.getCreatedBy())
                    .group(group)
                    .build();

            events.add(event);
        }

        // 이벤트 모두 저장
        List<MeetingEvent> saved = eventRepository.saveAll(events);

        return saved.stream()
                .map(MeetingResponseDto::fromEntity)
                .toList();
    }

    /**
     * 모든 회의 이벤트 조회
     */
    public List<MeetingResponseDto> getAllMeetings() {
        return eventRepository.findAll().stream()
                .map(MeetingResponseDto::fromEntity)
                .toList();
    }

    /**
     * 단일 이벤트 삭제
     */
    @Transactional
    public void deleteOne(UUID eventId) {
        eventRepository.deleteById(eventId);
    }

    /**
     * 반복 그룹 삭제 (모든 연관된 이벤트도 삭제)
     */
    @Transactional
    public void deleteAllInGroup(UUID groupId) {
        List<MeetingEvent> events = eventRepository.findByGroupId(groupId);
        eventRepository.deleteAll(events);
        groupRepository.deleteById(groupId);
    }
}
