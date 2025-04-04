package kr.co.direa.external.dto;

public class SseMessageDto {
    private String content;

    public SseMessageDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
