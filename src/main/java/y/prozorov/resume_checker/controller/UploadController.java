package y.prozorov.resume_checker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.service.UploadService;
import y.prozorov.resume_checker.util.JwtUtil;
import y.prozorov.resume_checker.util.ResponseUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin("${allowed.origins}")
public class UploadController {

    private final UploadService uploadService;
    private final JwtUtil jwtUtil;
    public UploadController(UploadService uploadService, JwtUtil jwtUtil) {
        this.uploadService = uploadService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Map<String,String>>> uploadFile(
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) {

        log.info("Started file upload: {}", file.getOriginalFilename());

        UUID userId = jwtUtil.extractUserId(jwt);
        Resume resume = uploadService.uploadFile(file, userId);

        Map<String,String> response = new HashMap<>();
        response.put("resumeId", resume.getId().toString());
        log.info("File uploaded successfully: {}", file.getOriginalFilename());

        return ResponseEntity.ok(ResponseUtil.success("File uploaded successfully", response, null));


    }


}
