package kr.co.direa.bookmark.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;
    private String name;
    private String url;

    @Builder
    public Bookmark(UUID userId, String name, String url) {
        this.userId = userId;
        this.name = name;
        this.url = url;
    }
}
