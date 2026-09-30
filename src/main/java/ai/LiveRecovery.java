package ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;

public final class LiveRecovery {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public BrowserAction recover(WebDriver driver, String requirement, BrowserAction failedAction) throws Exception {
        BrowserContext context = BrowserContextCollector.capture(driver);
        String prompt = MultimodalPromptBuilder.build(context, requirement)
                + "\nA Selenium action failed. Find a reliable replacement locator using the current DOM and screenshot."
                + "\nFailed action: " + failedAction
                + "\nReturn ONLY JSON for one BrowserAction with fields: action, strategy, locator, value, pageObject, elementName, methodName."
                + "\nKeep the same action and value. Change only the locator strategy/locator when possible.";
        String raw = client.generateWithImage(prompt, ImageEvidence.asDataUrl(context));
        return mapper.readValue(AIResponseParser.extractText(raw).trim(), BrowserAction.class);
    }
}
