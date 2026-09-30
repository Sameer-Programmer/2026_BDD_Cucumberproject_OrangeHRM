package ai;

public final class BrowserContextPrompt {
    private BrowserContextPrompt() { }

    public static String build(BrowserContext context) {
        return """
                Use the following live browser evidence when designing Selenium locators.
                Do not invent elements that are not supported by the HTML.
                Prefer stable attributes such as id, name, aria-label, data-* and accessible semantics.
                Avoid brittle absolute XPath.

                URL: %s
                Title: %s

                HTML:
                %s
                """.formatted(context.url(), context.title(), context.html());
    }
}
