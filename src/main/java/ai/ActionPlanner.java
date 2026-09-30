package ai;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ActionPlanner {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public ActionPlan plan(String requirement, BrowserContext context) throws Exception {
        String prompt = MultimodalPromptBuilder.build(context, requirement)
                + "\nReturn ONLY JSON: {\\\"actions\\\":[{\\\"action\\\":\\\"click|type|select|verify|navigate\\\",\\\"strategy\\\":\\\"id|name|css|xpath|linkText\\\",\\\"locator\\\":\\\"...\\\",\\\"value\\\":\\\"...\\\"}]}";
        String raw = client.generateWithImage(prompt, ImageEvidence.asDataUrl(context));
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        return mapper.readValue(text, ActionPlan.class);
    }
}
