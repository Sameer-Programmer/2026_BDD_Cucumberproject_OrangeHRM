package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.util.List;



public class TableHelper {

    public WebDriver driver;

    public TableHelper(WebDriver driver){
        this.driver = driver;
    }


    // Returns total number of headers
    public int getHeaderCount(List<WebElement> headers) {
        return headers.size();
    }

    // Returns total number of rows
    public int getRowCount(List<WebElement> rows) {
        return rows.size();
    }

    // Prints all headers
    public void printHeaders(List<WebElement> headers) {

        System.out.println("========== HEADERS ==========");

        for (WebElement header : headers) {
            System.out.println(header.getText());
        }
    }

    // Prints all rows
    public void printRows(List<WebElement> rows) {

        System.out.println("========== ROWS ==========");

        for (WebElement row : rows) {
            System.out.println(row.getText());
        }
    }

    public boolean verifyCandidateAcrossPages(String vacancy,
                                              String candidateName,
                                              By rowLocator,
                                              WebElement nextButton) {

        while (true) {

            // Get fresh rows every time
            List<WebElement> rows = driver.findElements(rowLocator);

            // Search current page
            for (WebElement row : rows) {

                String rowText = row.getText();

                if (rowText.contains(vacancy)
                        && rowText.contains(candidateName)) {

                    return true;
                }
            }

            // Last page reached
            if (!nextButton.isEnabled()) {
                return false;
            }

            // Go to next page
            nextButton.click();

            // Small wait for table refresh
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}