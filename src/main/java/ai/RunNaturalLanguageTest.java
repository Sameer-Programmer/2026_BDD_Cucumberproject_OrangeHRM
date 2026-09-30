package ai;

import org.openqa.selenium.WebDriver;

public class RunNaturalLanguageTest {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Usage: RunNaturalLanguageTest <url> <natural-language-requirement>");
        }

        String url = args[0];
        String requirement = String.join(" ",
                java.util.Arrays.copyOfRange(args, 1, args.length));

        System.out.println("=== AI NATURAL-LANGUAGE TEST ===");
        System.out.println("URL: " + url);
        System.out.println("Requirement: " + requirement);

        WebDriver driver = BrowserAgent.startChrome(false);

        try {
            driver.get(url);

            PomRecorder recorder =
                    new IterativeAgent().run(driver, requirement, 15);

            System.out.println("\n=== EXECUTION COMPLETE ===");
            System.out.println("Recorded actions: " + recorder.actions().size());

            System.out.println("\n=== GENERATED TESTNG ===");
            System.out.println(
                    TestCodeGenerator.generate("GeneratedAITest", recorder.actions()));

        } finally {
            driver.quit();
        }
    }
}
