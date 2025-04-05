package y.prozorov.resume_checker.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.repository.ResumeRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private ResumeService resumeService;

    private UUID userId;
    private Resume resume;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        resume = Resume.builder()
                .userId(userId)
                .resumeText("Resume Text")
                .updatedAt(Instant.now())
                .uploadedAt(Instant.now())
                .build();
    }

    @Test
    void save_success() {
        when(resumeRepository.save(resume)).thenReturn(resume);

        var savedResume = resumeService.save(resume);

        verify(resumeRepository).save(resume);
        assertNotNull(savedResume);
        assertEquals(resume,savedResume);
    }

    @Test
    void getResumeByUserId_success(){
        when(resumeRepository.findByUserId(userId)).thenReturn(Optional.of(resume));

        var result = resumeService.getResumeByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("Resume Text", result.getResumeText());
        verify(resumeRepository).findByUserId(userId);
    }

    @Test
    void getResumeByUserId_notFound_throwsException() {
        when(resumeRepository.findByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> resumeService.getResumeByUserId(userId)
        );

        assertEquals("Resume not found for userId: " + userId, thrown.getMessage());
        verify(resumeRepository).findByUserId(userId);
    }

    @Test
    void hasResume_returnsTrue() {
        when(resumeRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = resumeService.hasResume(userId);

        assertTrue(result);
        verify(resumeRepository).existsByUserId(userId);
    }


    @Test
    void hasResume_returnsFalse() {
        when(resumeRepository.existsByUserId(userId)).thenReturn(false);

        boolean result = resumeService.hasResume(userId);

        assertFalse(result);
        verify(resumeRepository).existsByUserId(userId);
    }

}