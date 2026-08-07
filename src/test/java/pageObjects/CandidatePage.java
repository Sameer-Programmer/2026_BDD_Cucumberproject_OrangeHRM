package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.TableHelper;
import utilities.WaitHelper;
import org.openqa.selenium.By;
import java.util.List;


public class CandidatePage {

    WebDriver driver;
    private WaitHelper waitHelper;
    private TableHelper tableHelper;

    public CandidatePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        waitHelper = new WaitHelper(driver);
        tableHelper = new TableHelper(driver);
    }

    // ==========================================================
    // Locators
    // ==========================================================

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

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement btnSearch;

    @FindBy(xpath = "//h5[normalize-space()='Candidates']")
    WebElement txtCandidatesHeader;

    @FindBy(xpath = "//p[contains(normalize-space(),'Successfully Saved')]")
    WebElement txtSuccessMessage;

    // ==========================================================
    // Table Locators
    // ==========================================================

    @FindBy(xpath = "//div[@role='table']")
    WebElement tblCandidates;

    @FindBy(xpath = "//div[@role='columnheader']")
    List<WebElement> columnHeaders;

    @FindBy(xpath = "//div[@class='oxd-table-card']")
    List<WebElement> tableRows;

    @FindBy(css = ".oxd-icon.bi-chevron-right")
    WebElement btnNextPage;

    // ==========================================================
    // Action Methods
    // ==========================================================

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

    public void clickSearchButton() {
        btnSearch.click();
    }

    public void moveToTable() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", tblCandidates);
    }

    // ==========================================================
    // Verification Methods
    // ==========================================================

    public boolean isCandidatesHeaderDisplayed() {
        return txtCandidatesHeader.isDisplayed();
    }

    public String getCandidatesHeaderText() {
        return txtCandidatesHeader.getText();
    }

    public boolean isSuccessMessageDisplayed() {
        waitHelper.waitforElement(txtSuccessMessage,10);
        return txtSuccessMessage.isDisplayed();
    }

    public String getSuccessMessage() {
        waitHelper.waitforElement(txtSuccessMessage,10);
        return txtSuccessMessage.getText();
    }


    // ==========================================================
    // Table Helper Methods
    // ==========================================================

    public boolean verifyCandidate(String vacancy, String candidateName) {

        return tableHelper.verifyCandidateAcrossPages(
                vacancy,
                candidateName,
                By.xpath("//div[@class='oxd-table-card']"),
                btnNextPage);
    }

    public WebElement getTable() {
        return tblCandidates;
    }

    public List<WebElement> getColumnHeaders() {
        return columnHeaders;
    }

    public List<WebElement> getTableRows() {
        return tableRows;
    }

    public WebElement getNextPageButton() {
        return btnNextPage;
    }

    public int getHeaderCount() {
        return tableHelper.getHeaderCount(columnHeaders);
    }

    public int getRowCount() {
        return tableHelper.getRowCount(tableRows);
    }

    public void printHeaders() {
        tableHelper.printHeaders(columnHeaders);
    }

    public void printRows() {
        tableHelper.printRows(tableRows);
    }
}