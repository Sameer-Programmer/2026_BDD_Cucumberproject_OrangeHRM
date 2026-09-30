package ai;

import java.util.List;

public record FailureAnalysis(String summary, String rootCause, String suggestedFix, List<RepairFile> repairs) { }
