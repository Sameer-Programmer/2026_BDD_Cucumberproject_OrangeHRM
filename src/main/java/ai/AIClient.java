package ai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** Sends text and multimodal prompts to the OpenAI Responses API. */
public class AIClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey;
    private final String model;

    public AIClient() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        this.model = System.getenv().getOrDefault("OPENAI_MODEL", "gpt-5.6-luna");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY environment variable is not set.");
        }
    }

    public String generate(String prompt) throws IOException, InterruptedException {
        String escapedPrompt = escape(prompt);
        String body = "{\"model\":\"" + escape(model)
                + "\",\"input\":\"" + escapedPrompt + "\"}";

        return send(body);
    }

    public String generateWithImage(String prompt, String imageDataUrl)
            throws IOException, InterruptedException {
        String body = "{\"model\":\"" + escape(model)
                + "\",\"input\":[{\"role\":\"user\",\"content\":["
                + "{\"type\":\"input_text\",\"text\":\"" + escape(prompt) + "\"},"
                + "{\"type\":\"input_image\",\"image_url\":\"" + escape(imageDataUrl) + "\"}"
                + "]}]}";

        return send(body);
    }

    private String send(String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "AI API request failed: HTTP " + response.statusCode()
                            + " - " + response.body());
        }
        return response.body();
    }

    private static String escape(String value) {
        return value == null ? "" : value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
