package kr.co.direa.workspace.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Table(name = "bookmarks")
@Entity
@NoArgsConstructor
public class Bookmark extends BaseTimeEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;

    @Builder
    public Bookmark(UUID userId, String name, String url) {
        this.userId = userId;
        this.name = name;
        this.url = url;
    }
}
