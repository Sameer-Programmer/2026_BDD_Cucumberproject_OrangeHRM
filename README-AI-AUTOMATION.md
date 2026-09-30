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
