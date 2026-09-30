package ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/** Extracts the text output from an OpenAI Responses API JSON response. */
public final class AIResponseParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private AIResponseParser() { }

    public static String extractText(String json) throws Exception {
        JsonNode root = MAPPER.readTree(json);
        JsonNode output = root.path("output");
        List<String> parts = new ArrayList<>();
        for (JsonNode item : output) {
            for (JsonNode content : item.path("content")) {
                if (content.has("text")) {
                    parts.add(content.path("text").asText());
                }
            }
        }
        if (parts.isEmpty()) {
            throw new IllegalStateException("No text output was returned by the AI model.");
        }
        return String.join("\n", parts);
    }
}
