package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.WaitHelper;

public class HomePage {
    public WebDriver driver;
    private final WaitHelper waitHelper;

    // Default explicit wait (seconds) for this page's clicks/visibility checks.
    // Mirrors the explicitWait value in config.properties.
    private static final long EXPLICIT_WAIT_SECONDS = 20L;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h6[text()='Dashboard']")
    WebElement dashboardText;

    @FindBy(linkText = "Logout")
    @CacheLookup
    WebElement lnkLogout;

    @FindBy(css = ".oxd-topbar-header-userarea")
    WebElement userprofile;

    @FindBy(xpath ="(//span[contains(normalize-space(),'Recruitment')])[1]")
    WebElement recruitment;




    public boolean isDashboardDisplayed() {
        waitHelper.waitforElement(dashboardText, EXPLICIT_WAIT_SECONDS);
        return dashboardText.isDisplayed();
    }

    public void clickLogout() {
        waitHelper.waitForElementToBeClickable(userprofile, EXPLICIT_WAIT_SECONDS).click();
        waitHelper.waitForElementToBeClickable(lnkLogout, EXPLICIT_WAIT_SECONDS).click();
    }

    public void clickRecruitment() {
        // Wait for the element to actually be present+visible+enabled before clicking.
        // A bare .click() with no wait fires as soon as the page object is constructed,
        // before the sidebar has finished rendering/attaching handlers -> ElementNotInteractable.
        WebElement recruitmentMenu = waitHelper.waitForElementToBeClickable(recruitment, EXPLICIT_WAIT_SECONDS);

        // Scroll it into view first as a safety net (harmless if already in view,
        // helps in headless runs or smaller viewports where it may sit below the fold).
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", recruitmentMenu);

        recruitmentMenu.click();
    }
}