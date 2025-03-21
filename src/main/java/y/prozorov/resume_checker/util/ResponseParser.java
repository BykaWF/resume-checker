package y.prozorov.resume_checker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import y.prozorov.resume_checker.model.Suggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ResponseParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Suggestion> parseSuggestions( ResponseEntity<Map<String, Object>> response ) {

        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String content = (String) message.get("content");

        content = cleanJsonContent(content);

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(content,
                    new TypeReference<List<Suggestion>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing suggestions : {}", e.getMessage());
            return parseManually(content);
        }
    }

    private static List<Suggestion> parseManually(String content) {
        log.info("Starting manual parsing of content");
        List<Suggestion> result = new ArrayList<>();

        try {
            Pattern pattern = Pattern.compile("\\{\\s*\"missingKeyword\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"suggestion\"\\s*:\\s*\"([^\"]+)\"\\s*\\}");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String keyword = matcher.group(1);
                String suggestion = matcher.group(2);
                result.add(new Suggestion(keyword, suggestion));
            }
        } catch (Exception e) {
            log.error("Error in manual parsing fallback", e);
        }

        return result;
    }

    private static String cleanJsonContent(String content) {
        content = content.replaceAll(",\\s*]", "]");

        content = content.replaceAll("\\{\\s*\\}\\s*]", "]");

        if (!content.trim().startsWith("[")) {
            content = "[" + content;
        }
        if (!content.trim().endsWith("]")) {
            content = content + "]";
        }

        return content;
    }
}