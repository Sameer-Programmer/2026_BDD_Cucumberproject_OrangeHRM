package ai;

import java.nio.file.Path;

public class ReviewGeneratedCode {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) throw new IllegalArgumentException("Provide a natural-language requirement.");
        String requirement = String.join(" ", args);
        String context = ProjectContext.summarize(Path.of(".").toAbsolutePath().normalize());
        GeneratedProject project = new GenerationService().generate(requirement, context);
        for (GeneratedFile file : project.files()) {
            System.out.println("\n===== " + file.path() + " =====\n");
            System.out.println(file.content());
        }
    }
}
