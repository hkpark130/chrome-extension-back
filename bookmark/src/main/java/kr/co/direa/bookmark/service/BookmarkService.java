package kr.co.direa.bookmark.service;

import kr.co.direa.bookmark.dto.BookmarkRequestDto;
import kr.co.direa.bookmark.dto.BookmarkResponseDto;
import kr.co.direa.bookmark.repository.BookmarkRepository;
import kr.co.direa.bookmark.entity.Bookmark;
import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto) {
        Bookmark bookmark = Bookmark.builder()
                .userId(requestDto.getUserId())  // ✅ 유저 ID 저장
                .name(requestDto.getName())
                .url(requestDto.getUrl())
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return BookmarkResponseDto.fromEntity(savedBookmark);
    }

    public List<BookmarkResponseDto> getUserBookmarks(UUID userId) {
        return bookmarkRepository.findByUserId(userId).stream()
                .map(BookmarkResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookmarkResponseDto editBookmarkById(UUID userId, Long bookmarkId, BookmarkRequestDto requestDto) {
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(bookmarkId, userId)
                .orElseThrow(() -> new CustomException(
                        CustomErrorCode.NOT_FOUND_BOOKMARK, "해당 사용자의 북마크를 찾을 수 없음 ID: " + bookmarkId));

        bookmark.setName(requestDto.getName());
        bookmark.setUrl(requestDto.getUrl());

        return BookmarkResponseDto.fromEntity(bookmark);
    }

    @Transactional
    public void deleteBookmarkById(UUID userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(bookmarkId, userId)
                .orElseThrow(() -> new CustomException(
                        CustomErrorCode.NOT_FOUND_BOOKMARK, "해당 사용자의 북마크를 찾을 수 없음 ID: " + bookmarkId));

        bookmarkRepository.delete(bookmark);
    }
}