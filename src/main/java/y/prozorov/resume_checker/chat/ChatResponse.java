package y.prozorov.resume_checker.chat;

import java.util.List;

public record ChatResponse(String id, String object, long created, String model, List<Choice> choices, Usage usage) {

    public record Choice(int index,Message message) {}

    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}

    public String getContent() {
        if (choices != null && !choices.isEmpty()) {
            Message msg = choices.getFirst().message();
            return (msg != null) ? msg.content() : null;
        }
        return null;
    }

}