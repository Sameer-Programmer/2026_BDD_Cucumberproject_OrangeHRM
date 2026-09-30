package ai;

import org.openqa.selenium.WebDriver;

public final class ResilientActionExecutor {
    private ResilientActionExecutor() { }

    public static BrowserAction executeWithRecovery(WebDriver driver, String requirement, BrowserAction action, int maxRecoveryAttempts) throws Exception {
        BrowserAction current = action;
        Exception last = null;
        LiveRecovery recovery = new LiveRecovery();
        for (int attempt = 0; attempt <= maxRecoveryAttempts; attempt++) {
            try {
                ActionExecutor.execute(driver, new ActionPlan(java.util.List.of(current)));
                return current;
            } catch (Exception failure) {
                last = failure;
                if (attempt == maxRecoveryAttempts) break;
                System.out.println("Live action failed. Asking AI for locator recovery attempt " + (attempt + 1));
                current = recovery.recover(driver, requirement, current);
            }
        }
        throw last;
    }
}
