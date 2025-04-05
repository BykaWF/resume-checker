package y.prozorov.resume_checker.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import y.prozorov.resume_checker.exception.FileUploadException;
import y.prozorov.resume_checker.model.Resume;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UploadService {

    private final ResumeService resumeService;

    public UploadService(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @Transactional
    public Resume uploadFile(MultipartFile file, UUID userId) {
        log.info("Starting file upload process for user: {}", userId);

        if (userId == null) {
            log.error("User ID cannot be null.");
            throw new FileUploadException("User ID is required.");
        }

        if (file.isEmpty()) {
            log.error("No file provided.");
            throw new FileUploadException("No file provided.");
        }

        log.info("File provided: {}", file.getOriginalFilename());

        if (!isValidFileType(file)) {
            log.error("Invalid file type: {}", file.getContentType());
            throw new FileUploadException("Invalid file type. Only PDF is allowed.");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            log.error("File size exceeds the limit: {} bytes", file.getSize());
            throw new FileUploadException("File size exceeds the limit (5MB).");
        }

        try {
            log.info("Extracting text from the file...");
            var tika = new Tika();
            var resumeText = tika.parseToString(file.getInputStream());
            log.info("File text extracted successfully.");

            Optional<Resume> existingResume = resumeService.findByUserId(userId);

            Resume resume;
            if (existingResume.isPresent()) {
                log.info("Existing resume found for user {}. Updating resume.", userId);
                resume = existingResume.get();
                resume.setResumeText(resumeText);
            } else {
                log.info("No existing resume found for user {}. Creating new resume entry.", userId);
                resume = Resume.builder()
                        .userId(userId)
                        .resumeText(resumeText)
                        .build();
            }

            var savedResume = resumeService.save(resume);
            log.info("File uploaded and resume saved with ID: {}", savedResume.getId());

            return savedResume;

        } catch (IOException e) {
            log.error("IOException while processing the file: {}", e.getMessage(), e);
            throw new FileUploadException("Error processing the file.");
        } catch (TikaException e) {
            log.error("TikaException while extracting text from the file: {}", e.getMessage(), e);
            throw new FileUploadException("Error extracting text from the file.");
        }
    }

    private boolean isValidFileType(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/pdf");
    }

}

