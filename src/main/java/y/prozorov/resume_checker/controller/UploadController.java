package y.prozorov.resume_checker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.service.UploadService;
import y.prozorov.resume_checker.util.ResponseUtil;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Resume>> uploadFile(
            @RequestPart(value = "file") MultipartFile file) {

        log.info("Started file upload: {}", file.getOriginalFilename());
        Resume resume = uploadService.uploadFile(file);
        log.info("File uploaded successfully: {}", file.getOriginalFilename());

        return ResponseEntity.ok(ResponseUtil.success("File uploaded successfully", resume, null));


    }


}
