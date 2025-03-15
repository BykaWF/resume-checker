package y.prozorov.resume_checker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import y.prozorov.resume_checker.dto.ResumeAnalysisRequest;
import y.prozorov.resume_checker.dto.ResumeAnalysisResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/resume")
public class ResumeAnalysisController {
    private final ChatClient chatClient;

    public ResumeAnalysisController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @PostMapping("/analyze")
    public  String analyzeResume(@RequestBody ResumeAnalysisRequest request) {
        String prompt = """
            Analyze the following resume:
            
            %s
            
            Find common mistakes and suggest only improvements based on those mistakes.
          
           """.formatted(request.resumeText());

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return aiResponse;
    }


}