package ai;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PomCodeGenerator {
    private PomCodeGenerator() { }

    public static String generatePageObject(String className, List<RecordedAction> recorded) {
        StringBuilder out = new StringBuilder();
        out.append("package pageObjects;\n\n");
        out.append("import org.openqa.selenium.By;\nimport org.openqa.selenium.WebDriver;\n\n");
        out.append("public class ").append(className).append(" {\n");
        out.append("    private final WebDriver driver;\n\n");
        out.append("    public ").append(className).append("(WebDriver driver) { this.driver = driver; }\n\n");
        int index = 1;
        for (RecordedAction r : recorded) {
            BrowserAction a = r.action();
            if (a.locator() == null || a.locator().isBlank()) continue;
            String method = actionMethodName(a, index++);
            out.append("    public void ").append(method).append("() {\n");
            out.append("        driver.findElement(").append(byExpression(a)).append(").");
            if ("type".equalsIgnoreCase(a.action())) out.append("sendKeys(\"").append(escape(a.value())).append("\");\n");
            else out.append("click();\n");
            out.append("    }\n\n");
        }
        out.append("}\n");
        return out.toString();
    }

    private static String actionMethodName(BrowserAction a, int index) {
        String base = a.action().toLowerCase() + "Element" + index;
        return base.replaceAll("[^A-Za-z0-9_]", "");
    }

    private static String byExpression(BrowserAction a) {
        return switch (a.strategy().toLowerCase()) {
            case "id" -> "By.id(\"" + escape(a.locator()) + "\")";
            case "name" -> "By.name(\"" + escape(a.locator()) + "\")";
            case "css" -> "By.cssSelector(\"" + escape(a.locator()) + "\")";
            case "xpath" -> "By.xpath(\"" + escape(a.locator()) + "\")";
            case "linktext" -> "By.linkText(\"" + escape(a.locator()) + "\")";
            default -> throw new IllegalArgumentException("Unsupported locator: " + a.strategy());
        };
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
