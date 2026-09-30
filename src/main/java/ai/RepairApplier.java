package ai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public final class RepairApplier {
    private RepairApplier() { }

    public static void apply(FailureAnalysis analysis, Path root) throws Exception {
        if (analysis.repairs() == null) return;
        for (RepairFile repair : analysis.repairs()) {
            String relative = repair.path();
            if (relative == null || relative.isBlank() || relative.contains("..") || relative.startsWith("/") || relative.startsWith("\\")) {
                throw new SecurityException("Unsafe AI repair path: " + relative);
            }
            if (!relative.endsWith(".java") || !relative.startsWith("src/")) {
                throw new SecurityException("AI repair is limited to Java source under src/: " + relative);
            }
            Path target = root.resolve(relative).normalize();
            if (!target.startsWith(root.normalize())) throw new SecurityException("Repair escaped staging root");
            Files.createDirectories(target.getParent());
            Files.writeString(target, repair.content(), StandardCharsets.UTF_8);
        }
    }
}
