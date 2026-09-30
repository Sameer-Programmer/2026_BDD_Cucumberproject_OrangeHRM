package ai;

/** Builds the framework-specific instruction used by the AI generator. */
public class PromptManager {
    public String buildPrompt(String userRequirement) {
        return """
                You are a senior Java Selenium automation engineer.

                Project rules:
                - Java 21
                - Selenium 4.x
                - TestNG
                - Maven
                - Page Object Model
                - Keep locators in Page Objects, never in test classes
                - Reuse existing BaseClass and utilities when available
                - Prefer explicit waits over Thread.sleep()
                - Generate clean, compilable Java
                - Do not invent existing framework classes; ask for their contents when needed

                User automation requirement:
                %s

                Return ONLY valid JSON. The root object must contain a "files" array. Each item must contain exactly "path" and "content". Use repository-relative Java paths such as src/test/java/... or src/main/java/.... Do not wrap the JSON in Markdown fences. Never include secrets. Do not modify pom.xml unless explicitly requested.
                """.formatted(userRequirement);
    }
}
