package ai;

public record FailureAnalysis(String summary, String rootCause, String suggestedFix, String patch) { }
