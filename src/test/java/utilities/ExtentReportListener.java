package utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepFinished;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import stepDefiniations.Baseclass;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Captures Cucumber scenario results, including the failure exception, in ExtentReports. */
public class ExtentReportListener implements ConcurrentEventListener {
    private static final DateTimeFormatter SCREENSHOT_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");
    private static final ThreadLocal<Boolean> FAILURE_SCREENSHOT_CAPTURED =
            ThreadLocal.withInitial(() -> false);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        FAILURE_SCREENSHOT_CAPTURED.set(false);
        ExtentTest test = ExtentReportManager.createTest(event.getTestCase().getName());
        test.assignCategory("Cucumber");
        test.info("Scenario started");
    }

    /** Captures the browser before Cucumber performs any post-scenario cleanup. */
    private void onTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() != Status.FAILED
                || Boolean.TRUE.equals(FAILURE_SCREENSHOT_CAPTURED.get())) {
            return;
        }

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            FAILURE_SCREENSHOT_CAPTURED.set(attachFailureScreenshot(test));
        }
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            Status status = event.getResult().getStatus();
            if (status == Status.PASSED) {
                test.pass("Scenario passed");
            } else if (status == Status.SKIPPED || status == Status.PENDING || status == Status.UNDEFINED || status == Status.AMBIGUOUS) {
                test.skip("Scenario status: " + status);
            } else if (status == Status.FAILED) {
                Throwable error = event.getResult().getError();
                if (error == null) test.fail("Scenario failed");
                else test.fail(error);
                if (!Boolean.TRUE.equals(FAILURE_SCREENSHOT_CAPTURED.get())) {
                    FAILURE_SCREENSHOT_CAPTURED.set(attachFailureScreenshot(test));
                }
            } else {
                test.warning("Scenario status: " + status);
            }
        }

        ExtentReportManager.flushReport();
        ExtentReportManager.removeCurrentTest();
        FAILURE_SCREENSHOT_CAPTURED.remove();
    }

    /** Embeds the current browser image directly in the Extent HTML report. */
    private boolean attachFailureScreenshot(ExtentTest test) {
        WebDriver driver = Baseclass.driver;

        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            test.warning("Screenshot was not captured because no active browser was available.");
            return false;
        }

        try {
            Path screenshotsDirectory = Paths.get(System.getProperty("user.dir"), "Reports", "screenshots");
            Files.createDirectories(screenshotsDirectory);

            String screenshotFileName = "Failure_"
                    + LocalDateTime.now().format(SCREENSHOT_TIMESTAMP_FORMAT)
                    + ".png";
            Path screenshotPath = screenshotsDirectory.resolve(screenshotFileName);

            File temporaryScreenshot = screenshotDriver.getScreenshotAs(OutputType.FILE);
            Files.copy(temporaryScreenshot.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);

            // Some Chromium/Edge desktop sessions return a blank Selenium image.
            // When that happens, capture only the browser window as a fallback.
            if (isBlankImage(screenshotPath)) {
                captureBrowserWindow(driver, screenshotPath);
            }

            test.fail(MediaEntityBuilder
                    .createScreenCaptureFromPath("screenshots/" + screenshotFileName, "Failure Screenshot")
                    .build());
            return true;
        } catch (Exception exception) {
            test.warning("Screenshot capture failed: " + exception.getMessage());
            return false;
        }
    }

    private boolean isBlankImage(Path screenshotPath) throws IOException {
        BufferedImage image = ImageIO.read(screenshotPath.toFile());
        if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
            return true;
        }

        int minBrightness = 255;
        int maxBrightness = 0;
        for (int x = 0; x < image.getWidth(); x += Math.max(1, image.getWidth() / 20)) {
            for (int y = 0; y < image.getHeight(); y += Math.max(1, image.getHeight() / 20)) {
                int rgb = image.getRGB(x, y);
                int brightness = ((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF);
                minBrightness = Math.min(minBrightness, brightness / 3);
                maxBrightness = Math.max(maxBrightness, brightness / 3);
            }
        }
        return minBrightness > 245 && maxBrightness - minBrightness < 5;
    }

    private void captureBrowserWindow(WebDriver driver, Path screenshotPath) throws Exception {
        org.openqa.selenium.Point position = driver.manage().window().getPosition();
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        Rectangle browserBounds = new Rectangle(position.getX(), position.getY(), size.getWidth(), size.getHeight());
        BufferedImage browserImage = new Robot().createScreenCapture(browserBounds);
        ImageIO.write(browserImage, "png", screenshotPath.toFile());
    }
}
