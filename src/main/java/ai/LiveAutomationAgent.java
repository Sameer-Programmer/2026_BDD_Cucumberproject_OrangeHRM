package ai;

import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public class LiveAutomationAgent {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: LiveAutomationAgent <url> <requirement>");
        }
        String url = args[0];
        String requirement = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        WebDriver driver = BrowserAgent.startChrome(false);
        try {
            driver.get(url);
            BrowserContext context = BrowserContextCollector.capture(driver);
            String browserEvidence = MultimodalPromptBuilder.build(context, requirement);
            String projectContext = ProjectContext.summarize(Path.of(".").toAbsolutePath().normalize());
            String prompt = projectContext + "\n\nLIVE BROWSER EVIDENCE:\n" + browserEvidence;\n            String raw = new AIClient().generateWithImage(prompt, ImageEvidence.asDataUrl(context));\n            String text = AIResponseParser.extractText(raw);\n            System.out.println(text);\n            return;
            for (GeneratedFile file : project.files()) {
                System.out.println("\n===== " + file.path() + " =====\n");
                System.out.println(file.content());
            }
        } finally {
            driver.quit();
        }
    }
}
