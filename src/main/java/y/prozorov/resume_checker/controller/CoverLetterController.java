package y.prozorov.resume_checker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.dto.ApplicationContentRequest;
import y.prozorov.resume_checker.service.ChatService;
import y.prozorov.resume_checker.util.JwtUtil;
import y.prozorov.resume_checker.util.ResponseUtil;

@RestController
@RequestMapping("/api/v1/cover-letter")
@CrossOrigin("${allowed.origins}")
public class CoverLetterController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CoverLetterController(ChatService chatService, JwtUtil jwtUtil) {
        this.chatService = chatService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/generate")
    public ApiResponse<String> generateCoverLetter(@RequestBody ApplicationContentRequest request,
                                                   @AuthenticationPrincipal Jwt jwt) {
        var userId = jwtUtil.extractUserId(jwt);
        var result = chatService.generateCoverLetter(request, userId);
        return ResponseUtil.success("Cover Letter Generated", result, null);
    }
}
