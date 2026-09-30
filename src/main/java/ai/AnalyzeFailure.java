package ai;

import java.nio.file.Files;
import java.nio.file.Path;

public class AnalyzeFailure {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) throw new IllegalArgumentException("Usage: AnalyzeFailure \"requirement\" \"failure-log\"");
        String requirement = args[0];
        String log = Files.readString(Path.of(args[1]));
        FailureAnalysis analysis = new FailureAnalyzer().analyze(requirement, log);
        System.out.println("Summary: " + analysis.summary());
        System.out.println("Root cause: " + analysis.rootCause());
        System.out.println("Suggested fix: " + analysis.suggestedFix());
        System.out.println("Proposed patch:\n" + analysis.patch());
    }
}
