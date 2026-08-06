package stepDefiniations;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import pageObjects.CandidatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import java.util.Random;

public class Baseclass {
    public WebDriver driver;
    public LoginPage loginPage;
    public HomePage homePage;
    public CandidatePage candidatePage;

    public static String  randomString(){
     String generatedString1 = RandomStringUtils.randomAlphabetic(5);
        return generatedString1;
    }

}
