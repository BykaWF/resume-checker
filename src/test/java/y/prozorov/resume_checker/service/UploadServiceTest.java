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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UploadServiceTest {
    @InjectMocks
    private UploadService uploadService; // Your class with uploadFile method

    @Mock
    private ResumeService resumeService; // Mock the ResumeService

    @Mock
    private MultipartFile file; // Mock the MultipartFile

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testUploadFileSuccess() throws IOException {
        // Given
        String resumeText = "This is a dummy resume text";
        Resume mockedResume = Resume.builder()
                .userId(UUID.randomUUID())
                .resumeText(resumeText)
                .build();

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(resumeText.getBytes()));

        when(resumeService.save(any(Resume.class))).thenReturn(mockedResume);

        // When
        Resume result = uploadService.uploadFile(file);

        // Then
        assertNotNull(result);
        assertEquals(mockedResume.getResumeText(), result.getResumeText());
        assertEquals(mockedResume.getUserId(), result.getUserId());
    }

    @Test
    void testUploadFileNoFileProvided() {
        // Given
        when(file.isEmpty()).thenReturn(true);

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file));
        assertEquals("No file provided.", exception.getMessage());
    }

    @Test
    void testUploadFileInvalidFileType() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file));
        assertEquals("Invalid file type.", exception.getMessage());
    }

    @Test
    void testUploadFileSizeExceedsLimit() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(10 * 1024 * 1024L);

        // When & Then
        FileUploadException exception = assertThrows(FileUploadException.class, () -> uploadService.uploadFile(file));
        assertEquals("File size exceeds the limit.", exception.getMessage());
    }
}