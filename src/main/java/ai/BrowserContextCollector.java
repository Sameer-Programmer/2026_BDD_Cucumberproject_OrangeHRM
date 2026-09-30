package ai;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Base64;

public final class BrowserContextCollector {
    private BrowserContextCollector() { }

    public static BrowserContext capture(WebDriver driver) {
        String screenshot = "";
        if (driver instanceof TakesScreenshot ts) {
            byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
            screenshot = Base64.getEncoder().encodeToString(bytes);
        }
        return new BrowserContext(
                driver.getCurrentUrl(),
                driver.getTitle(),
                driver.getPageSource(),
                screenshot
        );
    }
}
