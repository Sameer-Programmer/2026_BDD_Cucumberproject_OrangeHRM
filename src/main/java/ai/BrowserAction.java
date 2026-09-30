package ai;

public record BrowserAction(
        String action,
        String strategy,
        String locator,
        String value,
        String pageObject,
        String elementName,
        String methodName
) { }
