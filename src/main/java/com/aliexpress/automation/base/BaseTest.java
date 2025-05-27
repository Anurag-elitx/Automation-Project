package com.aliexpress.automation.base;

import com.aliexpress.automation.utils.ConfigReader;
import com.aliexpress.automation.utils.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Base test class that provides common functionality for all test classes.
 * Handles WebDriver initialization, setup, and teardown operations.
 */
public class BaseTest {
    protected WebDriver driver;
    protected static final Logger LOGGER = Logger.getLogger(BaseTest.class.getName());
    protected ConfigReader configReader = new ConfigReader();

    @BeforeSuite
    public void beforeSuite() {
        LOGGER.info("Starting test suite execution");
    }

    @BeforeClass
    public void beforeClass() {
        LOGGER.info("Starting test class execution: " + this.getClass().getSimpleName());
    }

    @BeforeMethod
    public void setup() {
        String browser = configReader.getProperty("browser", "chrome");
        initializeDriver(browser);
        
        String url = configReader.getProperty("base.url", "https://aliexpress.com");
        driver.get(url);
        LOGGER.info("Navigated to URL: " + url);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            LOGGER.warning("Test Failed: " + result.getName());
            ScreenshotUtils.captureScreenshot(driver, result.getName());
        } else {
            LOGGER.info("Test Completed: " + result.getName() + " with status: " + getResultStatusName(result.getStatus()));
        }
        
        if (driver != null) {
            driver.quit();
            LOGGER.info("WebDriver closed successfully");
        }
    }

    @AfterClass
    public void afterClass() {
        LOGGER.info("Completed test class execution: " + this.getClass().getSimpleName());
    }

    @AfterSuite
    public void afterSuite() {
        LOGGER.info("Completed test suite execution");
    }

    /**
     * Initializes the WebDriver based on the specified browser
     *
     * @param browser Browser name to initialize
     */
    private void initializeDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        LOGGER.info("Initialized " + browser + " browser");
    }

    /**
     * Gets the textual representation of the test result status
     *
     * @param status TestNG status code
     * @return String representation of test status
     */
    private String getResultStatusName(int status) {
        switch (status) {
            case ITestResult.SUCCESS:
                return "SUCCESS";
            case ITestResult.FAILURE:
                return "FAILURE";
            case ITestResult.SKIP:
                return "SKIP";
            default:
                return "UNKNOWN";
        }
    }
}