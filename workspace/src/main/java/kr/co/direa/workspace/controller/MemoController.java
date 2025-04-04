package kr.co.direa.workspace.controller;

import kr.co.direa.common.response.ApiResponse;
import kr.co.direa.workspace.dto.MemoDto;
import kr.co.direa.workspace.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
@Slf4j
public class MemoController {
    private final MemoService memoService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemoDto>> getMemo(@PathVariable UUID userId) {
        MemoDto memoDto = memoService.getMemo(userId);

        return ResponseEntity.ok(ApiResponse.success(memoDto));
    }

    @PostMapping
    public void saveMemo(@RequestBody MemoDto memo) {
        memoService.saveMemo(memo);
    }
}
