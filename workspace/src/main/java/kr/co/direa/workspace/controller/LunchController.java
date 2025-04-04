package kr.co.direa.workspace.controller;

import kr.co.direa.common.response.ApiResponse;
import kr.co.direa.workspace.dto.LunchDto;
import kr.co.direa.workspace.service.LunchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lunch")
@RequiredArgsConstructor
@Slf4j
public class LunchController {
    private final LunchService lunchService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<LunchDto>> getRandomLunch() {
        LunchDto lunch = lunchService.getRandomLunch();
        return ResponseEntity.ok(ApiResponse.success(lunch));
    }
}
