package ai;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public final class ActionExecutor {
    private ActionExecutor() { }

    public static void execute(WebDriver driver, ActionPlan plan) {
        for (BrowserAction action : plan.actions()) {
            switch (action.action().toLowerCase()) {
                case "navigate" -> driver.get(action.value());
                case "click" -> find(driver, action).click();
                case "type" -> {
                    WebElement e = find(driver, action);
                    e.clear();
                    e.sendKeys(action.value());
                }
                case "select" -> {
                    WebElement e = find(driver, action);
                    new Select(e).selectByVisibleText(action.value());
                }
                case "verify" -> {
                    if (!driver.getPageSource().contains(action.value())) {
                        throw new AssertionError("Expected text not found: " + action.value());
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported AI action: " + action.action());
            }
        }
    }

    private static WebElement find(WebDriver driver, BrowserAction a) {
        return switch (a.strategy().toLowerCase()) {
            case "id" -> driver.findElement(By.id(a.locator()));
            case "name" -> driver.findElement(By.name(a.locator()));
            case "css" -> driver.findElement(By.cssSelector(a.locator()));
            case "xpath" -> driver.findElement(By.xpath(a.locator()));
            case "linktext" -> driver.findElement(By.linkText(a.locator()));
            default -> throw new IllegalArgumentException("Unsupported locator strategy: " + a.strategy());
        };
    }
}
