package kr.co.direa.workspace.controller;

import jakarta.validation.Valid;
import kr.co.direa.workspace.dto.MeetingRequestDto;
import kr.co.direa.workspace.dto.MeetingResponseDto;
import kr.co.direa.workspace.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    /**
     * 회의 일정 등록 (단일 or 반복)
     */
    @PostMapping
    public ResponseEntity<List<MeetingResponseDto>> createMeeting(@Valid @RequestBody MeetingRequestDto requestDto) {
        List<MeetingResponseDto> created = meetingService.createMeeting(requestDto);
        return ResponseEntity.ok(created);
    }

    /**
     * 전체 회의 이벤트 조회
     */
    @GetMapping
    public ResponseEntity<List<MeetingResponseDto>> getAllMeetings() {
        List<MeetingResponseDto> events = meetingService.getAllMeetings();
        return ResponseEntity.ok(events);
    }

    /**
     * 단일 회의 삭제
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID eventId) {
        meetingService.deleteOne(eventId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 반복 그룹 삭제
     */
    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
        meetingService.deleteAllInGroup(groupId);
        return ResponseEntity.noContent().build();
    }

}
