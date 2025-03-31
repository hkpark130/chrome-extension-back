package kr.co.direa.workspace.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table(name = "meeting_group")
@Entity
@NoArgsConstructor
public class MeetingGroup extends BaseTimeEntity{
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "repeat_weeks")
    private int repeatWeeks;

    @Column(name = "recurrence_type")
    private String recurrenceType = "WEEKLY"; // DAILY, WEEKLY, MONTHLY 등

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingEvent> events;

    @Builder
    public MeetingGroup(UUID id, int repeatWeeks, String recurrenceType, List<MeetingEvent> events) {
        this.id = id != null ? id : UUID.randomUUID();
        this.repeatWeeks = repeatWeeks;
        this.recurrenceType = recurrenceType != null ? recurrenceType : "WEEKLY";
        this.events = events;
    }

}
