package kr.co.direa.bookmark.service;

import kr.co.direa.bookmark.dto.BookmarkResponseDto;
import kr.co.direa.bookmark.repository.BookmarkRepository;
import kr.co.direa.bookmark.entity.Bookmark;
import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkResponseDto getBookmarkById(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_BOOKMARK, "Bookmark ID: " + id));
        return BookmarkResponseDto.fromEntity(bookmark);
    }
}