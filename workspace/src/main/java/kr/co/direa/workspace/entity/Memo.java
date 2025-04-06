package kr.co.direa.workspace.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "memo")
@NoArgsConstructor
public class Memo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private UUID userId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Memo(UUID userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
