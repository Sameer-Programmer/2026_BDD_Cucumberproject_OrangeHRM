package ai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GenerateAndTest {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) throw new IllegalArgumentException("Provide a natural-language requirement.");
        String requirement = String.join(" ", args);
        Path root = Path.of(".").toAbsolutePath().normalize();
        String context = ProjectContext.summarize(root);
        GeneratedProject project = new GenerationService().generate(requirement, context);
        Path generatedRoot = root.resolve("generated-ai").normalize();
        if (Files.exists(generatedRoot)) deleteDirectory(generatedRoot);
        copyDirectory(root.resolve("src"), generatedRoot.resolve("src"));
        GeneratedProjectWriter.write(project, generatedRoot);
        Files.copy(root.resolve("pom.xml"), generatedRoot.resolve("pom.xml"), StandardCopyOption.REPLACE_EXISTING);
        Path testng = root.resolve("testng.xml");
        if (Files.exists(testng)) Files.copy(testng, generatedRoot.resolve("testng.xml"), StandardCopyOption.REPLACE_EXISTING);

        final int maxRepairAttempts = 3;
        for (int attempt = 0; attempt <= maxRepairAttempts; attempt++) {
            MavenExecutionResult result = MavenExecutor.test(generatedRoot, 10);
            System.out.println("\n=== MAVEN ATTEMPT " + (attempt + 1) + " ===");
            System.out.println(result.output());
            if (result.passed()) {
                System.out.println("AI-generated project passed Maven tests.");
                return;
            }
            if (attempt == maxRepairAttempts) {
                System.out.println("Repair limit reached. Stopping safely.");
                return;
            }
            FailureAnalysis analysis = new FailureAnalyzer().analyze(requirement, result.output());
            System.out.println("AI root cause: " + analysis.rootCause());
            if (analysis.repairs() == null || analysis.repairs().isEmpty()) {
                System.out.println("AI returned no safe repair files. Stopping.");
                return;
            }
            RepairApplier.apply(analysis, generatedRoot);
            System.out.println("Applied " + analysis.repairs().size() + " repair file(s).");
        }
    }

    private static void copyDirectory(Path source, Path target) throws Exception {
        try (var stream = Files.walk(source)) {
            stream.forEach(path -> {
                try {
                    Path destination = target.resolve(source.relativize(path));
                    if (Files.isDirectory(path)) Files.createDirectories(destination);
                    else {
                        Files.createDirectories(destination.getParent());
                        Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception e) { throw new RuntimeException(e); }
            });
        }
    }

    private static void deleteDirectory(Path directory) throws Exception {
        try (var stream = Files.walk(directory)) {
            stream.sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
                try { Files.deleteIfExists(path); }
                catch (Exception e) { throw new RuntimeException(e); }
            });
        }
    }
}
