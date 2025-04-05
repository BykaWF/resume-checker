package y.prozorov.resume_checker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import y.prozorov.resume_checker.chat.ChatClient;
import y.prozorov.resume_checker.dto.ApplicationContentRequest;
import y.prozorov.resume_checker.exception.GenerateResponseException;
import y.prozorov.resume_checker.model.CoverLetter;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.util.PromptBase;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatService {

    private final ResumeService resumeService;
    private final ChatClient chatClient;
    private final CoverLetterService coverLetterService;
    private final PromptBase promptBase;

    @Autowired
    public ChatService(ResumeService resumeService, ChatClient chatClient, CoverLetterService coverLetterService, PromptBase promptBase) {
        this.resumeService = resumeService;
        this.chatClient = chatClient;
        this.coverLetterService = coverLetterService;
        this.promptBase = promptBase;
    }

    public List<Suggestion> generateSuggestions(ApplicationContentRequest request, UUID userId) {
        log.info("Starting resume generate suggestions for user ID: {}", userId);

        try {
            var resume = resumeService.getResumeByUserId(userId);
            log.info("Retrieved resume for user ID: {}, resume length: {} characters",
                    userId, resume.getResumeText().length());

            var jobDescription = request.getJobDescription();
            log.info("Job description length: {} characters", jobDescription.length());

            log.debug("Resume text (truncated): {}", truncateForLogging(resume.getResumeText()));
            log.debug("Job description (truncated): {}", truncateForLogging(jobDescription));

            String prompt = """
                     Instructions:
                     [%s]
                     Resume:
                     [%s]
                     Job Description:
                     [%s]
                    """.formatted(promptBase.getAnalyseResumePrompt(), resume.getResumeText(), jobDescription);

            log.info("Generated prompt of length: {} characters", prompt.length());

            log.info("Sending request to chat");
            var suggestions = chatClient.fetchSuggestions(prompt);

            log.info("Received {} suggestions from  chat", suggestions.size());
            if (log.isDebugEnabled() && !suggestions.isEmpty()) {
                log.debug("First suggestion: missingKeyword='{}', suggestion='{}'",
                        suggestions.getFirst().missingKeyword(),
                        suggestions.getFirst().suggestion());
            }

            return suggestions;
        } catch (Exception e) {
            log.error("Error analyzing resume for user ID {}: {}", userId, e.getMessage(), e);
            throw new GenerateResponseException("Occurred exception while trying to analyze : " + e.getMessage());
        }
    }

    public String generateCoverLetter(ApplicationContentRequest request, UUID userId) {
        log.info("Starting generation cover latter for user ID: {}", userId);
        try {
            var templateCoverLetter = coverLetterService.getBaseTemplate();
            var resume = resumeService.getResumeByUserId(userId);

            log.info("Retrieved resume for user ID: {}, resume length: {} characters. And Cover Letter Template",
                    userId, resume.getResumeText().length());

            var prompt = mergeIntoPrompt(request, templateCoverLetter, resume);

            log.info("Generated prompt for cover letter of length: {} characters", prompt.length());

            var coverLetter = chatClient.fetchCoverLetter(prompt);

            if (log.isDebugEnabled() && !coverLetter.isEmpty()) {
                log.debug("Cover Letter content : {}", coverLetter);
            }

            return coverLetter;
        } catch (Exception e) {
            log.error("Error generating cover letter for user ID {}: {}", userId, e.getMessage(), e);
            throw new GenerateResponseException("Occurred exception while trying to generate cover letter : " + e.getMessage());
        }

    }

    private String mergeIntoPrompt(ApplicationContentRequest request, CoverLetter templateCoverLetter, Resume resume) {
        var letterText = templateCoverLetter.getLetterText();
        var jobDescription = request.getJobDescription();


        return """
                 Instructions:
                 [%s]
                 Cover Letter Template:
                 [%s]
                 Resume:
                 [%s]
                 Job Description:
                 [%s]
                """.formatted(promptBase.getGenerateCoverLetterPrompt(), letterText, resume.getResumeText(), jobDescription);
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
