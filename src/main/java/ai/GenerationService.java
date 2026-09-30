package ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class GenerationService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AIClient client = new AIClient();
    private final PromptManager prompts = new PromptManager();

    public GeneratedProject generate(String requirement, String projectContext) throws Exception {
        String prompt = prompts.buildPrompt(requirement) + "\n\nExisting project context:\n" + projectContext;
        String raw = client.generate(prompt);
        String text = AIResponseParser.extractText(raw).trim();
        if (text.startsWith("```")) throw new IllegalStateException("AI returned Markdown instead of JSON.");
        GeneratedProject project = mapper.readValue(text, GeneratedProject.class);
        List<String> errors = GenerationValidator.validate(project);
        if (!errors.isEmpty()) throw new IllegalStateException("Generated output rejected: " + String.join("; ", errors));
        return project;
    }
}
