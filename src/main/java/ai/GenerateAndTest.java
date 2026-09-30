package ai;

import java.nio.file.Path;

public class GenerateAndTest {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) throw new IllegalArgumentException("Provide a natural-language requirement.");
        String requirement = String.join(" ", args);
        Path root = Path.of(".").toAbsolutePath().normalize();
        String context = ProjectContext.summarize(root);
        GeneratedProject project = new GenerationService().generate(requirement, context);
        Path generatedRoot = root.resolve("generated-ai").normalize();
        GeneratedProjectWriter.write(project, generatedRoot);
        System.out.println("Generated files written to: " + generatedRoot);
        MavenExecutionResult result = MavenExecutor.test(generatedRoot, 10);
        System.out.println(result.output());
        System.out.println("Maven exit code: " + result.exitCode());
    }
}
