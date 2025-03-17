package kr.co.direa.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.co.direa.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
@Builder
public class BookmarkRequestDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "북마크 이름은 비어있을 수 없습니다.")
    @Size(max = 100, message = "북마크 이름은 100자 이하로 입력하세요.")
    private String name;

    @NotBlank(message = "URL은 필수 입력 값입니다.")
    @Pattern(regexp = "https?://.*", message = "유효한 URL 주소를 입력하세요.")
    private String url;

    public Bookmark toEntity() {
        return Bookmark.builder()
                .userId(UUID.fromString(this.userId)) // ✅ String → UUID 변환
                .name(this.name)
                .url(this.url)
                .build();
    }
}
