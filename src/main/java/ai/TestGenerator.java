package ai;

/** Entry point for turning a natural-language requirement into AI-generated automation guidance. */
public class TestGenerator {
    public static void main(String[] args) throws Exception {
        String requirement = args.length == 0
                ? "Create a Selenium test for logging into OrangeHRM and opening the Candidates page."
                : String.join(" ", args);

        PromptManager promptManager = new PromptManager();
        AIClient client = new AIClient();
        String response = client.generate(promptManager.buildPrompt(requirement));
        System.out.println(response);
    }
}
