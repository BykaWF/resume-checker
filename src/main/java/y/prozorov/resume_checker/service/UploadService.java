package y.prozorov.resume_checker.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import y.prozorov.resume_checker.exception.FileUploadException;
import y.prozorov.resume_checker.model.Resume;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UploadService {

    private final ResumeService resumeService;

    public UploadService(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    public Resume uploadFile(MultipartFile file) {
        log.info("Starting file upload process...");

        if (file.isEmpty()) {
            log.error("No file provided.");
            throw new FileUploadException("No file provided.");
        }

        log.info("File provided: {}", file.getOriginalFilename());

        if (!isValidFileType(file)) {
            log.error("Invalid file type: {}", file.getContentType());
            throw new FileUploadException("Invalid file type.");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            log.error("File size exceeds the limit: {} bytes", file.getSize());
            throw new FileUploadException("File size exceeds the limit.");
        }

        try {
            log.info("Extracting text from the file...");
            var tika = new Tika();
            String resumeText = tika.parseToString(file.getInputStream());
            log.info("File text extracted successfully.");

            Resume resume = Resume.builder()
                    .userId(UUID.randomUUID())
                    .resumeText(resumeText)
                    .build();

            log.info("Saving the resume to the database...");
            Resume savedResume = resumeService.save(resume);
            log.info("File uploaded and resume saved with ID: {}", savedResume.getUserId());

            return savedResume;

        } catch (IOException e) {
            log.error("IOException while processing the file: {}", e.getMessage(), e);
            throw new FileUploadException("Error uploading the file.");
        } catch (TikaException e) {
            log.error("TikaException while extracting text from the file: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean isValidFileType(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/pdf");
    }
}

