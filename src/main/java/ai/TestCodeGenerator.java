package ai;

import java.util.List;

public final class TestCodeGenerator {
    private TestCodeGenerator() { }

    public static String generate(String testClassName, List<RecordedAction> recorded) {
        StringBuilder out = new StringBuilder();
        out.append("package testCases;\n\n");
        out.append("import org.testng.annotations.Test;\n");
        out.append("import pageObjects.*;\nimport testBase.BaseClass;\n\n");
        out.append("public class ").append(testClassName).append(" extends BaseClass {\n\n");
        out.append("    @Test\n    public void generatedScenario() {\n");
        for (RecordedAction r : recorded) {
            BrowserAction a = r.action();
            String pageClass = r.page();
            if ("verify".equalsIgnoreCase(a.action())) continue;
            out.append("        new ").append(pageClass).append("(driver).");
            out.append(a.action().toLowerCase()).append("Element1();\n");
        }
        out.append("    }\n}\n");
        return out.toString();
    }
}
