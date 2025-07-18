package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.Properties;

public class DriverSetup {
    static WebDriver driver = null;

    /**
     * Initializes and returns a WebDriver instance based on the browser name
     * specified in the properties file.
     *
     * return WebDriver instance (ChromeDriver or EdgeDriver)
     */
    public static WebDriver getDriver() throws Exception{
        Properties props = PropertyLoader.getProperties();
        String browserName = props.getProperty("BROWSER_NAME");

        if (browserName == null) {
            System.out.println("ERROR: 'BROWSER_NAME' not specified in properties file.");
            return null;
        }

        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            default:
                throw new Exception("ERROR: Unsupported browser specified: " + browserName);
        }

        return driver;
    }
    public static void closeDriver(WebDriver driver) throws Exception {
        try {
            driver.quit();
            System.out.println("Success: Driver closed successfully!");
        }catch(Exception e) {
            throw new Exception("ERROR: An error occured while closing driver!");
        }

    }
}
