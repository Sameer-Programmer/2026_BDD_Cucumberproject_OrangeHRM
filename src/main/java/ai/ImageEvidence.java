package ai;

import java.util.Base64;

public final class ImageEvidence {
    private ImageEvidence() { }

    public static String asDataUrl(BrowserContext context) {
        if (context.screenshotBase64() == null || context.screenshotBase64().isBlank()) return "";
        return "data:image/png;base64," + context.screenshotBase64();
    }

    public static byte[] asBytes(BrowserContext context) {
        if (context.screenshotBase64() == null || context.screenshotBase64().isBlank()) return new byte[0];
        return Base64.getDecoder().decode(context.screenshotBase64());
    }
}
