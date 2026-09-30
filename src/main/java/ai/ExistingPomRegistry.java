package ai;

import java.nio.file.Path;
import java.util.List;

public final class ExistingPomRegistry {
    private ExistingPomRegistry() { }

    public static BrowserAction resolve(BrowserAction action, List<PageObjectScanner.PageMethod> methods) {
        if (action == null || action.locator() == null) return action;

        for (PageObjectScanner.PageMethod m : methods) {
            if (m.strategy().equalsIgnoreCase(action.strategy())
                    && m.locator().equals(action.locator())) {
                return new BrowserAction(
                        m.action(),
                        m.strategy(),
                        m.locator(),
                        action.value(),
                        m.page(),
                        m.element(),
                        m.method()
                );
            }
        }
        return action;
    }

    public static List<PageObjectScanner.PageMethod> scan(Path pageObjectsDir) throws Exception {
        return PageObjectScanner.scan(pageObjectsDir);
    }

    public static String pageFor(BrowserAction action, List<PageObjectScanner.PageMethod> methods) {
        BrowserAction resolved = resolve(action, methods);
        return resolved.pageObject();
    }
}
