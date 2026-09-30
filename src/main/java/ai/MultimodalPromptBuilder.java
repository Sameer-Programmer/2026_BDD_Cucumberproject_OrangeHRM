package ai;

public final class MultimodalPromptBuilder {
    private MultimodalPromptBuilder() { }

    public static String build(BrowserContext context, String requirement) {
        return """
                You are generating Java Selenium automation using Page Object Model.
                Use the live browser HTML as the primary source for locator evidence.
                The browser screenshot is available to the caller as base64 image data for multimodal-capable models.
                Do not invent controls that are not supported by the evidence.

                Requirement:
                %s

                URL: %s
                Title: %s

                HTML:
                %s
                """.formatted(requirement, context.url(), context.title(), context.html());
    }
}
