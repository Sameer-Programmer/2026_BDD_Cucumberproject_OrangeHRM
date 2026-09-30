package ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PomCodeGenerator {
    private PomCodeGenerator() { }

    public static String generatePageObject(String className, List<RecordedAction> recorded) {
        StringBuilder out = new StringBuilder();
        out.append("package pageObjects;\n\n");
        out.append("import org.openqa.selenium.WebDriver;\n");
        out.append("import org.openqa.selenium.WebElement;\n");
        out.append("import org.openqa.selenium.support.FindBy;\n");
        out.append("import org.openqa.selenium.support.PageFactory;\n");
        out.append("import org.openqa.selenium.support.ui.ExpectedConditions;\n");
        out.append("import org.openqa.selenium.support.ui.WebDriverWait;\n");
        out.append("import java.time.Duration;\n\n");
        out.append("public class ").append(className).append(" {\n\n");
        out.append("    private final WebDriver driver;\n");
        out.append("    private final WebDriverWait wait;\n\n");
        out.append("    public ").append(className).append("(WebDriver driver) {\n");
        out.append("        this.driver = driver;\n");
        out.append("        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));\n");
        out.append("        PageFactory.initElements(driver, this);\n");
        out.append("    }\n\n");

        Set<String> fields = new HashSet<>();
        Set<String> methods = new HashSet<>();

        for (RecordedAction r : recorded) {
            BrowserAction a = r.action();
            if (a.locator() == null || a.locator().isBlank()) continue;

            String field = safeIdentifier(firstNonBlank(a.elementName(), inferFieldName(a), "element"));
            String method = safeIdentifier(firstNonBlank(a.methodName(), inferMethodName(a), "performAction"));

            if (fields.add(field)) {
                out.append("    @FindBy(").append(findByExpression(a)).append(")\n");
                out.append("    private WebElement ").append(field).append(";\n\n");
            }

            if (methods.add(method)) appendMethod(out, a, field, method);
        }

        out.append("}\n");
        return out.toString();
    }

    private static void appendMethod(StringBuilder out, BrowserAction a, String field, String method) {
        if ("type".equalsIgnoreCase(a.action())) {
            out.append("    public void ").append(method).append("(String value) {\n");
            out.append("        wait.until(ExpectedConditions.visibilityOf(").append(field).append("));\n");
            out.append("        ").append(field).append(".clear();\n");
            out.append("        ").append(field).append(".sendKeys(value);\n");
            out.append("    }\n\n");
        } else if ("select".equalsIgnoreCase(a.action())) {
            out.append("    public void ").append(method).append("(String value) {\n");
            out.append("        wait.until(ExpectedConditions.visibilityOf(").append(field).append("));\n");
            out.append("        new org.openqa.selenium.support.ui.Select(").append(field).append(").selectByVisibleText(value);\n");
            out.append("    }\n\n");
        } else if ("verify".equalsIgnoreCase(a.action())) {
            out.append("    public boolean ").append(method).append("() {\n");
            out.append("        return wait.until(ExpectedConditions.visibilityOf(").append(field).append(")).isDisplayed();\n");
            out.append("    }\n\n");
        } else {
            out.append("    public void ").append(method).append("() {\n");
            out.append("        wait.until(ExpectedConditions.elementToBeClickable(").append(field).append(")).click();\n");
            out.append("    }\n\n");
        }
    }

    private static String findByExpression(BrowserAction a) {
        String value = escape(a.locator());
        return switch (a.strategy().toLowerCase()) {
            case "id" -> "id = \"" + value + "\"";
            case "name" -> "name = \"" + value + "\"";
            case "css" -> "css = \"" + value + "\"";
            case "xpath" -> "xpath = \"" + value + "\"";
            case "linktext" -> "linkText = \"" + value + "\"";
            default -> throw new IllegalArgumentException("Unsupported locator strategy: " + a.strategy());
        };
    }

    private static String inferFieldName(BrowserAction a) {
        String value = a.locator() == null ? "" : a.locator().toLowerCase();
        if (value.contains("username")) return "txtUsername";
        if (value.contains("password")) return "txtPassword";
        if (value.contains("firstname") || value.contains("first-name")) return "txtFirstName";
        if (value.contains("lastname") || value.contains("last-name")) return "txtLastName";
        if (value.contains("email")) return "txtEmail";
        if (value.contains("contact")) return "txtContactNumber";
        if ("click".equalsIgnoreCase(a.action())) return "btnElement";
        return "element";
    }

    private static String inferMethodName(BrowserAction a) {
        if ("type".equalsIgnoreCase(a.action())) return "enterValue";
        if ("select".equalsIgnoreCase(a.action())) return "selectValue";
        if ("click".equalsIgnoreCase(a.action())) return "clickElement";
        if ("verify".equalsIgnoreCase(a.action())) return "verifyElement";
        return "performAction";
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) if (value != null && !value.isBlank()) return value;
        return null;
    }

    private static String safeIdentifier(String value) {
        String result = value.replaceAll("[^A-Za-z0-9_]", "");
        if (result.isBlank()) result = "element";
        if (Character.isDigit(result.charAt(0))) result = "_" + result;
        return result;
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
