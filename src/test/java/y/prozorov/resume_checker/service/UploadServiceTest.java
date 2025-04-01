package y.prozorov.resume_checker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import y.prozorov.resume_checker.exception.FileUploadException;
import y.prozorov.resume_checker.model.Resume;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UploadServiceTest {
    @InjectMocks
    private UploadService uploadService;

    @Mock
    private ResumeService resumeService;

    @Mock
    private MultipartFile file;

    private UUID userId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }
    @Test
    void testUploadFileSuccess_NewResume() throws IOException {
        // Given
        String resumeText = "This is a dummy resume text";
        Resume newResume = Resume.builder()
                .userId(userId)
                .resumeText(resumeText)
                .build();

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(resumeText.getBytes()));

        when(resumeService.findByUserId(userId)).thenReturn(Optional.empty());
        when(resumeService.save(any(Resume.class))).thenReturn(newResume);

        // When
        Resume result = uploadService.uploadFile(file, userId);

        // Then
        assertNotNull(result);
        assertEquals(resumeText, result.getResumeText());
        assertEquals(userId, result.getUserId());

        verify(resumeService).findByUserId(userId);
        verify(resumeService).save(any(Resume.class));
    }
    @Test
    void testUploadFileNoFileProvided() {
        // Given
        when(file.isEmpty()).thenReturn(true);

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file, userId));
        assertEquals("No file provided.", exception.getMessage());
    }

    @Test
    void testUploadFileInvalidFileType() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file, userId));
        assertEquals("Invalid file type. Only PDF is allowed.", exception.getMessage());
    }

    @Test
    void testUploadFileSizeExceedsLimit() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(10 * 1024 * 1024L); // 10MB

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file, userId));
        assertEquals("File size exceeds the limit (5MB).", exception.getMessage());
    }

    @Test
    void testUploadFileNullUserId() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(1024L);

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file, null));
        assertEquals("User ID is required.", exception.getMessage());
    }
}