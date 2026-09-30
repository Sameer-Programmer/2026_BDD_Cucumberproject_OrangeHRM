package ai;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TestCodeGenerator {
    private TestCodeGenerator() { }

    public static String generate(String testClassName, List<RecordedAction> recorded) {
        StringBuilder out = new StringBuilder();
        out.append("package testcases;\n\n");
        out.append("import org.testng.annotations.Test;\n");
        out.append("import pageObjects.*;\n");
        out.append("import stepDefiniations.Baseclass;\n\n");
        out.append("public class ").append(testClassName).append(" extends Baseclass {\n\n");

        Map<String, String> pageVariables = new LinkedHashMap<>();
        for (RecordedAction r : recorded) {
            String page = r.page();
            if (page == null || page.isBlank()) continue;
            String variable = Character.toLowerCase(page.charAt(0)) + page.substring(1);
            pageVariables.putIfAbsent(page, variable);
        }

        for (Map.Entry<String, String> e : pageVariables.entrySet()) {
            out.append("    private ").append(e.getKey()).append(" ")
                    .append(e.getValue()).append(";\n");
        }
        if (!pageVariables.isEmpty()) out.append("\n");

        out.append("    @Test\n");
        out.append("    public void generatedScenario() {\n");

        for (Map.Entry<String, String> e : pageVariables.entrySet()) {
            out.append("        ").append(e.getValue()).append(" = new ")
                    .append(e.getKey()).append("(driver);\n");
        }
        if (!pageVariables.isEmpty()) out.append("\n");

        for (RecordedAction r : recorded) {
            BrowserAction a = r.action();
            if ("navigate".equalsIgnoreCase(a.action())) continue;

            String page = r.page();
            String variable = pageVariables.get(page);
            if (variable == null) continue;

            String method = a.methodName();
            if (method == null || method.isBlank()) continue;

            if ("verify".equalsIgnoreCase(a.action())) {
                out.append("        org.testng.Assert.assertTrue(")
                        .append(variable).append(".").append(method).append("());\n");
            } else if ("type".equalsIgnoreCase(a.action())
                    || "select".equalsIgnoreCase(a.action())) {
                out.append("        ").append(variable).append(".").append(method)
                        .append("("").append(escape(a.value())).append("");\n");
            } else {
                out.append("        ").append(variable).append(".").append(method).append("();\n");
            }
        }

        out.append("    }\n");
        out.append("}\n");
        return out.toString();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
