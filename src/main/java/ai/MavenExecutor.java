package ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public final class MavenExecutor {
    private MavenExecutor() { }

    public static MavenExecutionResult test(Path projectRoot, long timeoutMinutes) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(resolveMavenCommand());
        builder.directory(projectRoot.toFile());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        String output;
        try (var in = process.getInputStream()) {
            output = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        boolean finished = process.waitFor(timeoutMinutes, TimeUnit.MINUTES);
        if (!finished) {
            process.destroyForcibly();
            return new MavenExecutionResult(-1, output + "\nMaven execution timed out.");
        }
        return new MavenExecutionResult(process.exitValue(), output);
    }

    private static String resolveMavenCommand() {
        return System.getProperty("os.name", "").toLowerCase().contains("win") ? "mvn.cmd" : "mvn";
    }
}
