package ai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/** Reads a small, controlled set of project files so the model can follow the existing framework. */
public final class ProjectContext {
    private ProjectContext() { }

    public static String summarize(Path projectRoot) throws Exception {
        if (!Files.exists(projectRoot)) return "Project root not found: " + projectRoot;
        try (var stream = Files.walk(projectRoot)) {
            return stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java") || p.getFileName().toString().equals("pom.xml"))
                    .filter(p -> !p.toString().contains("target"))
                    .limit(40)
                    .map(p -> p.toString().replace(projectRoot.toString(), ""))
                    .collect(Collectors.joining("\n"));
        }
    }
}
