package y.prozorov.resume_checker.chat;

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
import java.util.Objects;

@Slf4j
@Component
public class ChatClient {

    @Value("${spring.ai.completion-url}")
    private String COMPLETION_URL;
    @Value("${spring.ai.openai.api-key}")
    private String API_KEY;

    private final RestClient restClient;

    @Autowired
    public ChatClient(RestClient restClient) {
        this.restClient = restClient;
    }


    public List<Suggestion> fetchSuggestions(String prompt) {
        var requestPayload = new ChatRequest(List.of(new Message("user", prompt)));
        
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

    public String fetchCoverLetter(String prompt) {
        var requestPayload = new ChatRequest(List.of(new Message("user", prompt)));

        ResponseEntity<ChatResponse> response = restClient.post()
                .uri(COMPLETION_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .retrieve()
                .toEntity(ChatResponse.class);

        return Objects.requireNonNull(response.getBody()).getContent();
    }

}
