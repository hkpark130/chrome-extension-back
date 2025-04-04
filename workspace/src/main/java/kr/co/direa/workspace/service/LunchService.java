package kr.co.direa.workspace.service;

import kr.co.direa.workspace.dto.BookmarkResponseDto;
import kr.co.direa.workspace.dto.LunchDto;
import kr.co.direa.workspace.entity.Lunch;
import kr.co.direa.workspace.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LunchService {
    private final LunchRepository lunchRepository;

    public LunchDto getRandomLunch() {
        List<Lunch> items = lunchRepository.findAll();

        if (items.isEmpty()) {
            return new LunchDto("메뉴가 없습니다.");
        }

        // 총 가중치 합계를 계산
        double totalWeight = 0.0;
        for (Lunch item : items) {
            totalWeight += item.getWeight();
        }

        // 랜덤 값 고르기 (0 ~ totalWeight 사이)
        double r = Math.random() * totalWeight;

        // 누적 가중치 기반 선택
        int idx = 0;
        for (; idx < items.size() - 1; ++idx) {
            r -= items.get(idx).getWeight();
            if (r <= 0.0) break;
        }

        return new LunchDto(items.get(idx).getName());
    }
}
