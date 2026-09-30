package ai;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FailureAnalyzer {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public FailureAnalysis analyze(String requirement, String executionOutput) throws Exception {
        String prompt = """
                You are debugging a Java Selenium/TestNG automation framework.
                Analyze the failure below.
                Do not claim a locator is correct unless supported by the evidence.
                Return ONLY JSON with exactly these fields:
                summary, rootCause, suggestedFix, patch.
                The patch must be a proposed Java change only; do not include secrets.

                Requirement:
                %s

                Maven/TestNG output:
                %s
                """.formatted(requirement, executionOutput);
        String raw = client.generate(prompt);
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        return mapper.readValue(text, FailureAnalysis.class);
    }
}
