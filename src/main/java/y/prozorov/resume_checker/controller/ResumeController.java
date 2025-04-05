package y.prozorov.resume_checker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.dto.ApplicationContentRequest;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.service.ChatService;
import y.prozorov.resume_checker.service.ResumeService;
import y.prozorov.resume_checker.util.JwtUtil;
import y.prozorov.resume_checker.util.ResponseUtil;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/resume")
@CrossOrigin("${allowed.origins}")
public class ResumeController {

    private final ChatService ChatService;
    private final ResumeService resumeService;
    private final JwtUtil jwtUtil;

    public ResumeController(ChatService ChatService, ResumeService resumeService, JwtUtil jwtUtil) {
        this.ChatService = ChatService;
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/check-resume")
    public ResponseEntity<ApiResponse<Boolean>> existResume(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwtUtil.extractUserId(jwt);
        var hasResume = resumeService.hasResume(userId);
        var message = hasResume ? "Resume exist" : "Resume doesn't exist";
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.success(message, hasResume, null));
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<List<Suggestion>>> analyzeResume(
            @RequestBody ApplicationContentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwtUtil.extractUserId(jwt);
        var suggestions = ChatService.generateSuggestions(request, userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.success("Resume analyzed", suggestions, null));
    }


}