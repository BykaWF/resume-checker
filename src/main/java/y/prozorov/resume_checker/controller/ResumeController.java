package y.prozorov.resume_checker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import y.prozorov.resume_checker.dto.AnalyzeResumeRequest;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.service.AnalyzeService;
import y.prozorov.resume_checker.util.ResponseUtil;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/resume")
public class ResumeController {

    private final AnalyzeService analyzeService;

    public ResumeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @GetMapping("/analyze")
    public ResponseEntity<ApiResponse<List<Suggestion>>> analyzeResume(@RequestBody AnalyzeResumeRequest request, @RequestHeader("userId") String userId) {

        List<Suggestion> suggestions = analyzeService.analyzeResume(request, userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.success("Resume analyzed", suggestions,null));
    }


}