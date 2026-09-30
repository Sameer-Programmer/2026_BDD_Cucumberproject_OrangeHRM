package ai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class NaturalLanguagePipeline {
    private NaturalLanguagePipeline() { }

    public static void run(String url, String requirement) throws Exception {
        Path root = Path.of(".").toAbsolutePath().normalize();
        Path generatedRoot = root.resolve("generated-ai").normalize();

        if (Files.exists(generatedRoot)) deleteDirectory(generatedRoot);
        copyDirectory(root.resolve("src"), generatedRoot.resolve("src"));
        Files.copy(root.resolve("pom.xml"), generatedRoot.resolve("pom.xml"),
                StandardCopyOption.REPLACE_EXISTING);

        Path testng = root.resolve("testng.xml");
        if (Files.exists(testng)) {
            Files.copy(testng, generatedRoot.resolve("testng.xml"),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Scanning existing Page Objects...");
        List<PageObjectScanner.PageMethod> existingPoms =
                ExistingPomRegistry.scan(root.resolve("src/test/java/pageObjects"));
        System.out.println("Discovered existing POM methods: " + existingPoms.size());

        var driver = BrowserAgent.startChrome(false);
        PomRecorder recorder;
        try {
            driver.get(url);
            recorder = new IterativeAgent().run(driver, requirement, 15);
        } finally {
            driver.quit();
        }

        writeGeneratedArtifacts(generatedRoot, recorder.actions(), existingPoms);

        for (int attempt = 0; attempt <= 3; attempt++) {
            MavenExecutionResult result = MavenExecutor.test(generatedRoot, 10);
            System.out.println("\n=== MAVEN VALIDATION ATTEMPT " + (attempt + 1) + " ===");
            System.out.println(result.output());

            if (result.passed()) {
                System.out.println("AI browser workflow generated and validated successfully.");
                return;
            }

            if (attempt == 3) {
                System.out.println("Repair limit reached. Generated project remains in: " + generatedRoot);
                return;
            }

            FailureAnalysis analysis =
                    new FailureAnalyzer().analyze(requirement, result.output());

            if (analysis.repairs() == null || analysis.repairs().isEmpty()) {
                System.out.println("AI returned no safe repair files. Stopping.");
                return;
            }

            RepairApplier.apply(analysis, generatedRoot);
            System.out.println("Applied " + analysis.repairs().size() + " repair file(s).");
        }
    }

    private static void writeGeneratedArtifacts(
            Path root,
            List<RecordedAction> actions,
            List<PageObjectScanner.PageMethod> existingPoms) throws Exception {

        Map<String, List<RecordedAction>> byPage = new LinkedHashMap<>();

        for (RecordedAction action : actions) {
            if (action.page() == null || action.page().isBlank()) continue;
            byPage.computeIfAbsent(action.page(), k -> new ArrayList<>()).add(action);
        }

        for (var entry : byPage.entrySet()) {
            boolean existingPage = existingPoms.stream()
                    .anyMatch(p -> p.page().equals(entry.getKey()));

            if (!existingPage) {
                String code = PomCodeGenerator.generatePageObject(
                        entry.getKey(), entry.getValue());

                Path target = root.resolve("src/test/java/pageObjects")
                        .resolve(entry.getKey() + ".java");

                Files.createDirectories(target.getParent());
                Files.writeString(target, code);
                System.out.println("Generated new POM: " + target);
            } else {
                System.out.println("Reusing existing POM: " + entry.getKey());
            }
        }

        String testCode = TestCodeGenerator.generate("GeneratedAITest", actions);
        Path testTarget = root.resolve("src/test/java/testcases/GeneratedAITest.java");
        Files.createDirectories(testTarget.getParent());
        Files.writeString(testTarget, testCode);
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
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static void deleteDirectory(Path directory) throws Exception {
        try (var stream = Files.walk(directory)) {
            stream.sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
