package ai;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class GeneratedBaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }

    @AfterMethod
    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
