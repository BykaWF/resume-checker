package y.prozorov.resume_checker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import y.prozorov.resume_checker.chat.ChatClient;
import y.prozorov.resume_checker.dto.ApplicationContentRequest;
import y.prozorov.resume_checker.exception.GenerateResponseException;
import y.prozorov.resume_checker.model.CoverLetter;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.util.PromptBase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ChatServiceTest {

    @Mock
    private CoverLetterService coverLetterService;

    @Mock
    private ResumeService resumeService;

    @Mock
    private ChatClient chatClient;

    @Mock
    private PromptBase promptBase;

    @InjectMocks
    private ChatService chatService;

    private UUID userId;
    private ApplicationContentRequest request;
    private List<Suggestion> suggestions;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();

        request = new ApplicationContentRequest();
        request.setJobDescription("Job description");

        var coverLetter = CoverLetter.builder()
                .id(UUID.randomUUID())
                .letterText("Cover Letter Template Text")
                .updatedAt(Instant.now())
                .uploadedAt(Instant.now())
                .build();

        var resume = Resume.builder()
                .resumeText("Resume Text")
                .userId(userId)
                .updatedAt(Instant.now())
                .uploadedAt(Instant.now())
                .build();

        suggestions = new ArrayList<>();
        suggestions.add(new Suggestion("missing keyword", "suggestion"));

        when(coverLetterService.getBaseTemplate()).thenReturn(coverLetter);
        when(resumeService.getResumeByUserId(userId)).thenReturn(resume);
        when(chatClient.fetchCoverLetter(anyString())).thenReturn("Generated cover letter");
        when(chatClient.fetchSuggestions(anyString())).thenReturn(suggestions);
    }

    @Test
    void testGenerateCoverLetter_successful() {
        String result = chatService.generateCoverLetter(request, userId);

        assertNotNull(result);
        assertEquals("Generated cover letter", result);

        verify(coverLetterService).getBaseTemplate();
        verify(resumeService).getResumeByUserId(userId);
        verify(chatClient).fetchCoverLetter(anyString());
    }

    @Test
    void testFetchSuggestions_successful() {
        List<Suggestion> result = chatService.generateSuggestions(request, userId);

        assertNotNull(result);
        assertEquals(result, suggestions);

        verify(resumeService).getResumeByUserId(userId);
        verify(chatClient).fetchSuggestions(anyString());
    }

    @Test
    public void testGenerateSuggestions_serviceException() {
        when(chatClient.fetchSuggestions(anyString())).thenThrow(new RuntimeException("Service error"));

        assertThrows(GenerateResponseException.class, () -> chatService.generateSuggestions(request, userId));

        verify(chatClient).fetchSuggestions(anyString());
    }

    @Test
    public void testGenerateCoverLetter_serviceException() {
        when(chatClient.fetchCoverLetter(anyString())).thenThrow(new RuntimeException("Service error"));

        assertThrows(GenerateResponseException.class, () -> chatService.generateCoverLetter(request, userId));

        verify(chatClient).fetchCoverLetter(anyString());
    }
}