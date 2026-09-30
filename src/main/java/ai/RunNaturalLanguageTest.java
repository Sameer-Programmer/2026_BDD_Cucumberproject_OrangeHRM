package ai;

public class RunNaturalLanguageTest {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Usage: RunNaturalLanguageTest <url> <natural-language-requirement>");
        }

        String url = args[0];
        String requirement = String.join(" ",
                java.util.Arrays.copyOfRange(args, 1, args.length));

        System.out.println("=== AI NATURAL-LANGUAGE AUTOMATION ===");
        System.out.println("URL: " + url);
        System.out.println("Requirement: " + requirement);

        NaturalLanguagePipeline.run(url, requirement);
    }
}
