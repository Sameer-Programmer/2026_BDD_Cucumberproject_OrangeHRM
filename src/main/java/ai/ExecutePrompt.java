package ai;

import org.openqa.selenium.WebDriver;

public class ExecutePrompt {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) throw new IllegalArgumentException("Usage: ExecutePrompt <url> <requirement>");
        String url = args[0];
        String requirement = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        WebDriver driver = BrowserAgent.startChrome(false);
        try {
            driver.get(url);
            BrowserContext context = BrowserContextCollector.capture(driver);
            ActionPlan plan = new ActionPlanner().plan(requirement, context);
            ActionExecutor.execute(driver, plan);
            System.out.println("AI action plan executed successfully: " + plan.actions().size() + " action(s).");
        } finally {
            driver.quit();
        }
    }
}
