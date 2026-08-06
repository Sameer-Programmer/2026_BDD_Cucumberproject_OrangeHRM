package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath= "//input[@placeholder='Username']")
    @CacheLookup
    WebElement txtUsername;

    @FindBy(xpath= "//input[@placeholder='Password']")
    @CacheLookup
    WebElement txtPassword;

    @FindBy(xpath = "//button[@type='submit']")
    @CacheLookup
    WebElement btnLogin;




    public void setUserName(String uname) {
        txtUsername.clear();
        txtUsername.sendKeys(uname);

    }

    public void setPassword(String pwd) {
        txtPassword.clear();
        txtPassword.sendKeys(pwd);
    }

    public void clickLogin() {
        btnLogin.click();
    }




}