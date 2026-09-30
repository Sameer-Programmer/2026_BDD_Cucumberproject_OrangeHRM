package ai;

import org.openqa.selenium.WebDriver;

public class RunAgent {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) throw new IllegalArgumentException("Usage: RunAgent <url> <requirement>");
        String url = args[0];
        String requirement = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        WebDriver driver = BrowserAgent.startChrome(false);
        try {
            driver.get(url);
            new IterativeAgent().run(driver, requirement, 12);
        } finally {
            driver.quit();
        }
    }
}
