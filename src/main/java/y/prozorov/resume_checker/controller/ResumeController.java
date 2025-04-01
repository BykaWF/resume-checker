package y.prozorov.resume_checker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import y.prozorov.resume_checker.dto.AnalyzeResumeRequest;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.service.AnalyzeService;
import y.prozorov.resume_checker.service.ResumeService;
import y.prozorov.resume_checker.util.JwtUtil;
import y.prozorov.resume_checker.util.ResponseUtil;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/resume")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    private final AnalyzeService analyzeService;
    private final ResumeService resumeService;
    private final JwtUtil jwtUtil;

    public ResumeController(AnalyzeService analyzeService, ResumeService resumeService, JwtUtil jwtUtil) {
        this.analyzeService = analyzeService;
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/check-resume")
    public ResponseEntity<ApiResponse<Boolean>> existResume(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = jwtUtil.extractUserId(jwt);
        var hasResume = resumeService.hasResume(userId);
        String message = hasResume ? "Resume exist" : "Resume doesn't exist";
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.success(message, hasResume, null));
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<List<Suggestion>>> analyzeResume(
            @RequestBody AnalyzeResumeRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = jwtUtil.extractUserId(jwt);
        List<Suggestion> suggestions = analyzeService.analyzeResume(request, userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.success("Resume analyzed", suggestions, null));
    }


}