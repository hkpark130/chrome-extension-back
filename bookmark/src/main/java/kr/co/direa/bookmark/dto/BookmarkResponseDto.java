package kr.co.direa.bookmark.dto;

import kr.co.direa.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkResponseDto {
    private Long id;
    private String name;
    private String url;

    public static BookmarkResponseDto fromEntity(Bookmark bookmark) {
        return BookmarkResponseDto.builder()
                .id(bookmark.getId())
                .name(bookmark.getName())
                .url(bookmark.getUrl())
                .build();
    }

}
