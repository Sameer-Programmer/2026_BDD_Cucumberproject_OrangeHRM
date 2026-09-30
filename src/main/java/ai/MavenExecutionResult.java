package ai;

public record MavenExecutionResult(int exitCode, String output) {
    public boolean passed() { return exitCode == 0; }
}
