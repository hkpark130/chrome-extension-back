package kr.co.direa.workspace.dto;

import kr.co.direa.workspace.entity.Bookmark;
import kr.co.direa.workspace.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MemoDto {
    private String content;
    private UUID userId;

    public static MemoDto fromEntity(Memo memo) {
        return MemoDto.builder()
                .content(memo.getContent())
                .build();
    }

}
