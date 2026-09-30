package ai;

import java.nio.file.Path;

/** Command-line entry point: mvn -q exec:java ... can call this class in a local checkout. */
public class GenerateAutomation {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: GenerateAutomation \"natural language requirement\"");
        }
        String requirement = String.join(" ", args);
        Path root = Path.of(".").toAbsolutePath().normalize();
        String projectFiles = ProjectContext.summarize(root);

        PromptManager prompts = new PromptManager();
        AIClient client = new AIClient();
        String prompt = prompts.buildPrompt(requirement)
                + "\n\nExisting project file paths:\n" + projectFiles
                + "\n\nReturn JSON with this shape only:"
                + "\\n{\\\"files\\\":[{\\\"path\\\":\\\"src/test/java/...\\\",\\\"content\\\":\\\"...\\\"}]}";

        String raw = client.generate(prompt);
        String text = AIResponseParser.extractText(raw);
        System.out.println(text);
        System.out.println("\\nReview the generated files before writing them to the repository.");
    }
}
