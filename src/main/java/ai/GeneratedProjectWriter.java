package ai;

import java.nio.file.Files;
import java.nio.file.Path;

public final class GeneratedProjectWriter {
    private GeneratedProjectWriter() { }

    public static void write(GeneratedProject project, Path root) throws Exception {
        var errors = GenerationValidator.validate(project);
        if (!errors.isEmpty()) throw new IllegalStateException(String.join("; ", errors));
        for (GeneratedFile file : project.files()) {
            Path target = root.resolve(file.path()).normalize();
            if (!target.startsWith(root.normalize())) throw new SecurityException("Unsafe generated path: " + file.path());
            Files.createDirectories(target.getParent());
            Files.writeString(target, file.content());
        }
    }
}
