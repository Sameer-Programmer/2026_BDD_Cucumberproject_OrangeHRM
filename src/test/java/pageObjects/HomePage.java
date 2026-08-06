package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    public WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(dashboardText));
        return dashboardText.isDisplayed();
    }

    public void clickLogout() {
        userprofile.click();
        lnkLogout.click();
    }

    public void clickRecruitment() {
        recruitment.click();
    }
}
