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
        GeneratedProjectWriter.write(project, generatedRoot);\n        java.nio.file.Files.copy(root.resolve("pom.xml"), generatedRoot.resolve("pom.xml"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);\n        if (java.nio.file.Files.exists(root.resolve("testng.xml"))) {\n            java.nio.file.Files.copy(root.resolve("testng.xml"), generatedRoot.resolve("testng.xml"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);\n        }
        System.out.println("Generated files written to: " + generatedRoot);
        MavenExecutionResult result = MavenExecutor.test(generatedRoot, 10);
        System.out.println(result.output());
        System.out.println("Maven exit code: " + result.exitCode());
    }
}
