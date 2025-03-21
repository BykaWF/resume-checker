package y.prozorov.resume_checker.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import y.prozorov.resume_checker.model.Suggestion;
import y.prozorov.resume_checker.util.ResponseParser;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OllamaClient {

    @Value("${spring.ai.completion-url}")
    private String COMPLETION_URL;
    @Value("${spring.ai.openai.api-key}")
    private String API_KEY;

    private final RestClient restClient;

    @Autowired
    public OllamaClient(RestClient restClient) {
        this.restClient = restClient;
    }


    public List<Suggestion> fetchSuggestions(String prompt) {

        ChatRequest requestPayload = new ChatRequest(List.of(new Message("user", prompt)));

        ResponseEntity<Map<String, Object>> response = restClient.post()
                .uri(COMPLETION_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
        return ResponseParser.parseSuggestions(response);
    }

    public record ChatRequest(List<Message> messages) {
    }

    public record Message(String role, String content) {
    }
}
