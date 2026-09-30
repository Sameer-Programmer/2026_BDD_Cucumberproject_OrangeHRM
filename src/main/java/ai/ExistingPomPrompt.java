package ai;

import java.util.List;

public final class ExistingPomPrompt {
    private ExistingPomPrompt() { }

    public static String build(List<PageObjectScanner.PageMethod> methods) {
        StringBuilder out = new StringBuilder();
        out.append("\nEXISTING PAGE OBJECT CATALOG:\n");
        if (methods == null || methods.isEmpty()) {
            out.append("No existing Page Object methods were discovered.\n");
            return out.toString();
        }

        for (PageObjectScanner.PageMethod m : methods) {
            out.append("- pageObject=").append(m.page())
                    .append(", method=").append(m.method())
                    .append(", action=").append(m.action())
                    .append(", elementName=").append(m.element())
                    .append(", strategy=").append(m.strategy())
                    .append(", locator=").append(m.locator())
                    .append("\n");
        }

        out.append("\nPOM SELECTION RULES:\n");
        out.append("1. Prefer an existing Page Object method when its locator matches the target element.\n");
        out.append("2. Preserve the discovered pageObject, elementName, and methodName when reusing it.\n");
        out.append("3. Do not invent a duplicate Page Object method for an existing matching locator.\n");
        out.append("4. If no existing method matches, create a semantic locator and method name.\n");
        return out.toString();
    }
}
