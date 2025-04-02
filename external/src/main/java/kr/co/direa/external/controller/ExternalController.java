package kr.co.direa.external.controller;

import kr.co.direa.external.service.GitlabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external")
@RequiredArgsConstructor
public class ExternalController {
    private final GitlabService gitlabService;

    @GetMapping("/projects")
    public String getProjects() {
        return gitlabService.getProjects();
    }
}
