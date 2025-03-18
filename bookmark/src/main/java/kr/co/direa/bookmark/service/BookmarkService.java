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

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkResponseDto getBookmarkById(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Bookmark ID {} not found. Throwing CustomException", id);
                    return new CustomException(CustomErrorCode.NOT_FOUND_BOOKMARK, "Bookmark ID: " + id);
                });
        return BookmarkResponseDto.fromEntity(bookmark);
    }

    @Transactional
    public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto) {
        Bookmark bookmark = requestDto.toEntity();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return BookmarkResponseDto.fromEntity(savedBookmark);
    }
}