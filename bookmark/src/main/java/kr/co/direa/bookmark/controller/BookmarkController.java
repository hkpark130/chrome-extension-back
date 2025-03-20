package kr.co.direa.bookmark.controller;

import kr.co.direa.bookmark.dto.BookmarkRequestDto;
import kr.co.direa.bookmark.dto.BookmarkResponseDto;
import kr.co.direa.bookmark.service.BookmarkService;
import kr.co.direa.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> getBookmark(@PathVariable Long id) {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.getBookmarkById(id);
        return ResponseEntity.ok(ApiResponse.success(bookmarkResponseDto));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> createBookmark(@RequestBody BookmarkRequestDto requestDto, HttpMethod httpMethod) {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.createBookmark(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(bookmarkResponseDto));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
