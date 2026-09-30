package ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;

public class IterativeAgent {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();

    public void run(WebDriver driver, String requirement, int maxSteps) throws Exception {
        for (int step = 1; step <= maxSteps; step++) {
            BrowserContext context = BrowserContextCollector.capture(driver);
            String prompt = MultimodalPromptBuilder.build(context, requirement)
                    + "\nStep " + step + " of " + maxSteps
                    + "\nReturn ONLY JSON with status, action, reason. status is CONTINUE or DONE."
                    + " action fields are action, strategy, locator, value. If DONE, action may be null.";
            String raw = client.generateWithImage(prompt, ImageEvidence.asDataUrl(context));
            AgentDecision decision = mapper.readValue(AIResponseParser.extractText(raw), AgentDecision.class);
            System.out.println("Step " + step + ": " + decision.status() + " - " + decision.reason());
            if ("DONE".equalsIgnoreCase(decision.status())) return;
            if (decision.action() == null) throw new IllegalStateException("CONTINUE without an action.");
            ActionExecutor.execute(driver, new ActionPlan(java.util.List.of(decision.action())));
        }
        throw new IllegalStateException("Agent safety limit reached: " + maxSteps);
    }
}
