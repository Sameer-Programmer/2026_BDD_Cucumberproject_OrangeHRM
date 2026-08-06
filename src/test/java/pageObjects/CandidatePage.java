package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CandidatePage {

    WebDriver driver;

    public CandidatePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ==========================
    // Locators
    // ==========================

    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement btnAdd;

    @FindBy(xpath = "//input[@placeholder='First Name']")
    WebElement txtFirstName;

    @FindBy(xpath = "//input[@placeholder='Middle Name']")
    WebElement txtMiddleName;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    WebElement txtLastName;

    @FindBy(xpath = "//label[normalize-space()='Vacancy']/following::div[contains(normalize-space(),'-- Select --')][1]")
    WebElement drpVacancy;

    @FindBy(xpath = "(//span[normalize-space()='Senior QA Lead'])[1]")
    WebElement optionSeniorQALead;

    @FindBy(xpath = "//label[normalize-space()='Email']/following::input[@placeholder='Type here'][1]")
    WebElement txtEmail;

    @FindBy(xpath = "//label[normalize-space()='Contact Number']/following::input[@placeholder='Type here']")
    WebElement txtContactNumber;

    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    WebElement btnSave;

    // Success Message

    @FindBy(xpath = "//p[contains(normalize-space(),'Successfully Saved')]")
    WebElement txtSuccessMessage;

    // ==========================
    // Action Methods
    // ==========================

    public void clickAddButton() {
        btnAdd.click();
    }

    public void enterFirstName(String firstName) {
        txtFirstName.clear();
        txtFirstName.sendKeys(firstName);
    }

    public void enterMiddleName(String middleName) {
        txtMiddleName.clear();
        txtMiddleName.sendKeys(middleName);
    }

    public void enterLastName(String lastName) {
        txtLastName.clear();
        txtLastName.sendKeys(lastName);
    }

    public void clickVacancyDropdown() {
        drpVacancy.click();
    }

    public void selectSeniorQALead() {
        optionSeniorQALead.click();
    }

    public void enterEmail(String email) {
        txtEmail.clear();
        txtEmail.sendKeys(email);
    }

    public void enterContactNumber(String contactNumber) {
        txtContactNumber.clear();
        txtContactNumber.sendKeys(contactNumber);
    }

    public void clickSaveButton() {
        btnSave.click();
    }
    public boolean isSuccessMessageDisplayed() {
        return txtSuccessMessage.isDisplayed();
    }
    public String getSuccessMessage() {
        return txtSuccessMessage.getText();
    }
}