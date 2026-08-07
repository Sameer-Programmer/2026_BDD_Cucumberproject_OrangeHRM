package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelper {
    public WebDriver driver;

    public WaitHelper(WebDriver driver){
        this.driver = driver;
    }

    public void waitforElement(WebElement element,long timeOutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Waits until the element is present, visible, AND enabled - needed before any
    // .click() call. visibilityOf alone isn't enough: an element can be visible
    // but still mid-render/disabled, which is what was causing ElementNotInteractable.
    public WebElement waitForElementToBeClickable(WebElement element, long timeOutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}