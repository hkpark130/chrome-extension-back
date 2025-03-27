package kr.co.direa.bookmark.controller;

import kr.co.direa.bookmark.dto.BookmarkRequestDto;
import kr.co.direa.bookmark.dto.BookmarkResponseDto;
import kr.co.direa.bookmark.service.BookmarkService;
import kr.co.direa.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@RefreshScope
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<BookmarkResponseDto>>> getUserBookmarks(@PathVariable UUID userId) {
        List<BookmarkResponseDto> bookmarks = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.success(bookmarks));
    }

    @PutMapping("/{userId}/{bookmarkId}")
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> editBookmark(
            @PathVariable UUID userId,
            @PathVariable Long bookmarkId,
            @Validated @RequestBody BookmarkRequestDto requestDto) {
        BookmarkResponseDto updatedBookmark = bookmarkService.editBookmarkById(userId, bookmarkId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(updatedBookmark));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> createBookmark(
            @Validated @RequestBody BookmarkRequestDto requestDto) {
        BookmarkResponseDto response = bookmarkService.createBookmark(requestDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{userId}/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(
            @PathVariable UUID userId,
            @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmarkById(userId, bookmarkId);
        return ResponseEntity.ok("Deleted");
    }
}
