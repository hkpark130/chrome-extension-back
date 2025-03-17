package kr.co.direa.bookmark.controller;

import kr.co.direa.bookmark.dto.BookmarkResponseDto;
import kr.co.direa.bookmark.entity.Bookmark;
import kr.co.direa.bookmark.service.BookmarkService;
import kr.co.direa.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

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
}
