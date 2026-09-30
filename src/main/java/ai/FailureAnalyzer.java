package ai;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FailureAnalyzer {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public FailureAnalysis analyze(String requirement, String executionOutput) throws Exception {
        String prompt = "Analyze this Java Selenium/TestNG failure. Return ONLY JSON with fields summary, rootCause, suggestedFix, repairs. repairs must be an array of complete replacement Java files with path and content. Only modify files under src/, never use ../ or absolute paths, never modify pom.xml or secrets, preserve POM/TestNG architecture. Requirement:\n" + requirement + "\nMaven output:\n" + executionOutput;
        String raw = client.generate(prompt);
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        return mapper.readValue(text, FailureAnalysis.class);
    }
}
