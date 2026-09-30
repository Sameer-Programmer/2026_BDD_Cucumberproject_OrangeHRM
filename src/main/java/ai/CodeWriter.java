package ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Utility for writing generated source files. */
public final class CodeWriter {
    private CodeWriter() { }

    public static void writeJavaFile(Path path, String source) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, source);
    }
}
