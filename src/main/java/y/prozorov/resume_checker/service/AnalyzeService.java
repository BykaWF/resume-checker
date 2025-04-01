package y.prozorov.resume_checker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import y.prozorov.resume_checker.client.OllamaClient;
import y.prozorov.resume_checker.dto.AnalyzeResumeRequest;
import y.prozorov.resume_checker.exception.AnalyzeResumeException;
import y.prozorov.resume_checker.model.Suggestion;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AnalyzeService {

    private final ResumeService resumeService;
    private final OllamaClient ollamaClient;

    @Autowired
    public AnalyzeService(ResumeService resumeService, OllamaClient ollamaClient) {
        this.resumeService = resumeService;
        this.ollamaClient = ollamaClient;
    }

    public List<Suggestion> analyzeResume(AnalyzeResumeRequest request, UUID userId) {
        log.info("Starting resume analysis for user ID: {}", userId);

        try {
            var resume = resumeService.getResumeByUserId(userId);
            log.info("Retrieved resume for user ID: {}, resume length: {} characters",
                    userId, resume.getResumeText().length());

            String jobDescription = request.getJobDescription();
            log.info("Job description length: {} characters", jobDescription.length());

            log.debug("Resume text (truncated): {}", truncateForLogging(resume.getResumeText()));
            log.debug("Job description (truncated): {}", truncateForLogging(jobDescription));

            String prompt = """
                 Resume:
                 [%s]
                 Job Description:
                 [%s]
                """.formatted(resume.getResumeText(), jobDescription);

            log.info("Generated prompt of length: {} characters", prompt.length());

            log.info("Sending request to  client");
            List<Suggestion> suggestions = ollamaClient.fetchSuggestions(prompt);

            log.info("Received {} suggestions from  client", suggestions.size());
            if (log.isDebugEnabled() && !suggestions.isEmpty()) {
                log.debug("First suggestion: missingKeyword='{}', suggestion='{}'",
                        suggestions.getFirst().missingKeyword(),
                        suggestions.getFirst().suggestion());
            }

            return suggestions;
        } catch (Exception e) {
            log.error("Error analyzing resume for user ID {}: {}", userId, e.getMessage(), e);
            throw new AnalyzeResumeException("Occurred exception while trying to analyze : " + e.getMessage());
        }
    }

    private String truncateForLogging(String text) {
        final int MAX_LENGTH = 100;
        if (text == null) {
            return "null";
        }
        if (text.length() <= MAX_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_LENGTH) + "... [truncated, total length: " + text.length() + "]";
    }

}
