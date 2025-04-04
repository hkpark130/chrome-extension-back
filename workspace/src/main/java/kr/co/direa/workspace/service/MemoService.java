package kr.co.direa.workspace.service;

import kr.co.direa.common.exception.CustomException;
import kr.co.direa.common.exception.code.CustomErrorCode;
import kr.co.direa.workspace.dto.MemoDto;
import kr.co.direa.workspace.entity.Memo;
import kr.co.direa.workspace.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoDto getMemo(UUID userId) {
        return memoRepository.findByUserId(userId)
                .map(MemoDto::fromEntity)
                .orElseGet(() -> {
                    Memo newMemo = new Memo(userId, "");
                    return MemoDto.fromEntity(memoRepository.save(newMemo));
                });
    }

    public void saveMemo(MemoDto memoDto) {
        Memo memo = memoRepository.findByUserId(memoDto.getUserId())
                .orElseThrow(() -> new CustomException(
                        CustomErrorCode.NOT_FOUND_MEMO, "해당 사용자의 메모를 찾을 수 없음 ID: "
                        + memoDto.getUserId()));

        memo.setContent(memoDto.getContent());

        memoRepository.save(memo);
    }
}
