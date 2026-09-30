package ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;

public class IterativeAgent {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public PomRecorder run(WebDriver driver, String requirement, int maxSteps) throws Exception {
        PomRecorder recorder = new PomRecorder();

        for (int step = 1; step <= maxSteps; step++) {
            BrowserContext context = BrowserContextCollector.capture(driver);
            String prompt = MultimodalPromptBuilder.build(context, requirement)
                    + "\nStep " + step + " of " + maxSteps
                    + "\nReturn ONLY JSON with status, action, reason. status is CONTINUE or DONE."
                    + " action fields are action, strategy, locator, value, pageObject, elementName, methodName."
                    + " If DONE, action may be null.";

            String raw = client.generateWithImage(prompt, ImageEvidence.asDataUrl(context));
            AgentDecision decision = mapper.readValue(
                    AIResponseParser.extractText(raw),
                    AgentDecision.class
            );

            System.out.println("Step " + step + ": " + decision.status() + " - " + decision.reason());

            if ("DONE".equalsIgnoreCase(decision.status())) {
                return recorder;
            }

            if (decision.action() == null) {
                throw new IllegalStateException("CONTINUE without an action.");
            }

            BrowserAction executedAction = ResilientActionExecutor.executeWithRecovery(driver, requirement, decision.action(), 2);

            BrowserAction action = ExistingPomRegistry.resolve(executedAction);
            if (!"navigate".equalsIgnoreCase(action.action())
                    && !"verify".equalsIgnoreCase(action.action())) {
                String page = ExistingPomRegistry.pageFor(action);
                if (page == null || page.isBlank()) {
                    page = inferPageObject(context);
                }
                recorder.record(page, action);
            }
        }

        throw new IllegalStateException("Agent safety limit reached: " + maxSteps);
    }

    private static String inferPageObject(BrowserContext context) {
        String title = context.title() == null ? "" : context.title();
        String safe = title.replaceAll("[^A-Za-z0-9]", "");
        return safe.isBlank() ? "GeneratedPage" : safe + "Page";
    }
}
