# AI Selenium Automation Layer

This branch adds an AI-assisted generation layer on top of the existing Java/Selenium/TestNG framework.

## Flow

Natural-language requirement -> PromptManager -> AIClient -> structured generation -> review -> Java POM/TestNG files.

## Environment

Set the API key locally; never commit it:

- OPENAI_API_KEY
- OPENAI_MODEL (optional)

## Safety

Generated source is printed for review in this first version. It is intentionally not written directly into production source or committed automatically.

## Example requirement

Create automation for logging into OrangeHRM and opening the Candidates page using the existing Page Object Model and configuration utilities.

## Next phase

Add a strict JSON schema, generated-file validation, Maven compilation, test execution, and AI-assisted failure analysis.

### Self-healing / failure analysis

The failure-analysis layer accepts a Maven/TestNG log and asks the AI to identify the likely root cause and propose a Java patch. It is intentionally review-first: the patch is printed and is not automatically applied.


## Natural-Language Browser Execution

Run the AI browser agent with one natural-language requirement:

```bash
mvn -q -DskipTests compile
java -cp target/classes:target/dependency/* ai.RunNaturalLanguageTest "https://opensource-demo.orangehrmlive.com/" "Open the application, log in, go to Recruitment, add a candidate, save it, and verify the success message."
```

The agent:
1. Opens the supplied URL.
2. Captures the live DOM and screenshot.
3. Sends the requirement plus the discovered existing Page Object catalog to the AI.
4. Executes the planned Selenium action.
5. Uses bounded AI locator recovery if an action fails.
6. Records successful actions using existing Page Object methods where available.
7. Prints the generated TestNG test.

The browser agent is bounded to 15 steps and live locator recovery is bounded to 2 retries per failed action.
