package ai;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class GenerationValidator {
    private GenerationValidator() { }
    public static List<String> validate(GeneratedProject project) {
        List<String> errors = new ArrayList<>();
        if (project == null || project.files() == null || project.files().isEmpty()) {
            errors.add("AI returned no files."); return errors;
        }
        for (GeneratedFile file : project.files()) {
            if (file.path() == null || file.path().isBlank()) errors.add("Missing file path.");
            else {
                Path path = Path.of(file.path()).normalize();
                if (path.isAbsolute() || path.startsWith("..")) errors.add("Unsafe path: " + file.path());
                if (!file.path().endsWith(".java")) errors.add("Only .java files are allowed: " + file.path());
            }
            if (file.content() == null || file.content().isBlank()) errors.add("Empty source: " + file.path());
            if (file.content() != null && (file.content().contains("OPENAI_API_KEY=") || file.content().contains("sk-"))) errors.add("Possible secret detected: " + file.path());
        }
        return errors;
    }
}
