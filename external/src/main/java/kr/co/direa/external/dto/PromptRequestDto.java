package kr.co.direa.external.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class PromptRequestDto {
    @NotBlank(message = "프롬프트는 비어있을 수 없습니다.")
    private String message;

}
