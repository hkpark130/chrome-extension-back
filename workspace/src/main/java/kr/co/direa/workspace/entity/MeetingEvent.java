package kr.co.direa.workspace.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "meeting_event")
@Entity
@NoArgsConstructor
public class MeetingEvent extends BaseTimeEntity{
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private MeetingGroup group;

    @Column(name = "created_by")
    private UUID createdBy;

    @Builder
    public MeetingEvent(UUID id, String title, LocalDateTime start, LocalDateTime end, MeetingGroup group, UUID createdBy) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.group = group;
        this.createdBy = createdBy;
    }
}
