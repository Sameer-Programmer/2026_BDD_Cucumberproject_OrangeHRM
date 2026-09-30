package ai;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ActionPlanner {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public ActionPlan plan(String requirement, BrowserContext context) throws Exception {
        String prompt = MultimodalPromptBuilder.build(context, requirement)
                + "\nReturn ONLY JSON: {\"actions\":[{"
                + "\"action\":\"click|type|select|verify|navigate\","
                + "\"strategy\":\"id|name|css|xpath|linkText\","
                + "\"locator\":\"...\","
                + "\"value\":\"...\","
                + "\"pageObject\":\"LoginPage|HomePage|CandidatePage|...\","
                + "\"elementName\":\"semantic field name in lowerCamelCase\","
                + "\"methodName\":\"semantic method name in lowerCamelCase\"}]}"
                + "\nFor each UI action, choose a maintainable semantic page object, element name, and method name. "
                + "Examples: txtUsername/setUserName, btnLogin/clickLogin, txtFirstName/enterFirstName, btnSave/clickSaveButton. "
                + "Never use names like clickElement1 or typeElement2. "
                + "For navigate/verify actions pageObject, elementName, methodName may be null.";
        String raw = client.generateWithImage(prompt, ImageEvidence.asDataUrl(context));
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        return mapper.readValue(text, ActionPlan.class);
    }
}
