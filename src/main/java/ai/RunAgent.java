package ai;

import org.openqa.selenium.WebDriver;

public class RunAgent {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: RunAgent <url> <requirement>");
        }

        String url = args[0];
        String requirement = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        WebDriver driver = BrowserAgent.startChrome(false);
        try {
            driver.get(url);

            PomRecorder recorder = new IterativeAgent().run(driver, requirement, 12);

            System.out.println("\n=== GENERATED PAGE OBJECTS ===");
            java.util.Map<String, java.util.List<RecordedAction>> byPage = new java.util.LinkedHashMap<>();

            for (RecordedAction action : recorder.actions()) {
                byPage.computeIfAbsent(action.page(), k -> new java.util.ArrayList<>()).add(action);
            }

            for (var entry : byPage.entrySet()) {
                System.out.println(PomCodeGenerator.generatePageObject(entry.getKey(), entry.getValue()));
            }

            System.out.println("\n=== GENERATED TEST ===");
            System.out.println(TestCodeGenerator.generate("GeneratedAITest", recorder.actions()));
        } finally {
            driver.quit();
        }
    }
}
