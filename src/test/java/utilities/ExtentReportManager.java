package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Creates one Extent report for the run and one test node per scenario. */
public final class ExtentReportManager {
    private static final DateTimeFormatter REPORT_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final ExtentReports EXTENT = createReport();
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ExtentReportManager() { }

    private static ExtentReports createReport() {
        try {
            Path directory = Paths.get(System.getProperty("user.dir"), "Reports");
            Files.createDirectories(directory);

            String reportName = "ExtentReport_"
                    + LocalDateTime.now().format(REPORT_TIMESTAMP_FORMAT)
                    + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(
                    directory.resolve(reportName).toString());
            spark.config().setDocumentTitle("OrangeHRM Automation Report");
            spark.config().setReportName("OrangeHRM BDD Cucumber Execution");

            ExtentReports extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "OrangeHRM Demo");
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            return extent;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create the Extent report", exception);
        }
    }

    public static ExtentTest createTest(String scenarioName) {
        ExtentTest test = EXTENT.createTest(scenarioName);
        CURRENT_TEST.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return CURRENT_TEST.get();
    }

    public static void flushReport() {
        EXTENT.flush();
    }

    public static void removeCurrentTest() {
        CURRENT_TEST.remove();
    }
}
