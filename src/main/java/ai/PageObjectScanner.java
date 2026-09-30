package ai;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PageObjectScanner {
    private PageObjectScanner() { }

    public record PageMethod(String page, String method, String strategy,
                             String locator, String action, String element) { }

    private static final Pattern CLASS = Pattern.compile("\\bclass\\s+(\\w+)");
    private static final Pattern FIND_BY = Pattern.compile(
            "@FindBy\\s*\\(\\s*(id|name|css|xpath|linkText)\\s*=\\s*\"([^\"]+)\"\\s*\\)");
    private static final Pattern FIELD = Pattern.compile(
            "(?:private|protected|public)?\\s*WebElement\\s+(\\w+)\\s*;");
    private static final Pattern METHOD = Pattern.compile(
            "public\\s+(?:void|boolean|String|WebElement)\\s+(\\w+)\\s*\\(([^)]*)\\)");

    public static List<PageMethod> scan(Path pageObjectsDir) throws Exception {
        List<PageMethod> result = new ArrayList<>();
        if (!Files.isDirectory(pageObjectsDir)) return result;
        try (var stream = Files.list(pageObjectsDir)) {
            for (Path file : stream.filter(p -> p.toString().endsWith(".java")).toList()) {
                scanFile(file, result);
            }
        }
        return result;
    }

    private static void scanFile(Path file, List<PageMethod> result) throws Exception {
        String source = Files.readString(file);
        Matcher cm = CLASS.matcher(source);
        if (!cm.find()) return;
        String page = cm.group(1);

        Matcher fm = FIND_BY.matcher(source);
        while (fm.find()) {
            Matcher fieldMatcher = FIELD.matcher(source.substring(fm.end()));
            if (!fieldMatcher.find()) continue;

            String field = fieldMatcher.group(1);
            int fieldEnd = fm.end() + fieldMatcher.end();
            Matcher mm = METHOD.matcher(source.substring(fieldEnd));
            if (!mm.find()) continue;

            String method = mm.group(1);
            String action = inferAction(method);
            result.add(new PageMethod(page, method, fm.group(1), fm.group(2), action, field));
        }
    }

    private static String inferAction(String method) {
        String m = method.toLowerCase();
        if (m.startsWith("is") || m.startsWith("verify") || m.startsWith("get")) return "verify";
        if (m.startsWith("set") || m.startsWith("enter") || m.startsWith("type")) return "type";
        if (m.startsWith("select")) return "select";
        return "click";
    }
}
