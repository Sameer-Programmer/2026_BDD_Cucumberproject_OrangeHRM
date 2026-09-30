package ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class LocatorAdvisor {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public List<LocatorSuggestion> suggest(BrowserContext context, String elementDescription) throws Exception {
        String prompt = BrowserContextPrompt.build(context)
                + "\n\nFind a reliable Selenium locator for this element:\n" + elementDescription
                + "\nReturn ONLY a JSON array. Each item must contain: elementName, strategy, locator, reasoning.";
        String raw = client.generate(prompt);
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        return mapper.readValue(text, mapper.getTypeFactory().constructCollectionType(List.class, LocatorSuggestion.class));
    }
}
