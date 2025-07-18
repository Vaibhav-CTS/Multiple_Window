package multiplewindow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.DriverSetup;
import utilities.PropertyLoader;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class Main {
    // Load properties and initialize WebDriver
    static Properties props = PropertyLoader.getProperties();
    static WebDriver driver;
    // Variables to store window handles
    static String parentWindow, childWindow;
    static Set<String> windowHandles = null;
    static int counter=0;

    // Launch the browser: Maximize window, set timeouts, and navigate to base URL
    static void navigateToBaseUrl() {
        if(!props.getProperty("BASE_URL").equals("https://www.rediff.com/")){
            throw new RuntimeException("ERROR: Not a correct baseURL");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // Enter the URL
            counter++;
            driver.get(props.getProperty("BASE_URL"));
        } catch (Exception e) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Unable to open the baseURL");
        }
        System.out.println("TC-"+counter+" || SUCCESS: baseURL is opened successfully");
    }

    // Click on the "Create Account" link
    static void navigateToAccountPage() {
        try {
            counter++;
            driver.findElement(By.linkText("Create Account")).click();
        } catch (Exception e) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Unable to locate the 'Create Account' link");
        }
        System.out.println("TC-"+counter+" || SUCCESS: locate the 'Create Account' link");
    }

    // Validate that the "Create Rediffmail account" page is opened
    static void validateAccountPageIsOpen() {
        try {
            counter++;
            driver.findElement(By.xpath("//div[@class='cnt']/h2"));
        } catch (Exception e) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Create \"Rediffmail account\" webpage not opened");
        }
        System.out.println("TC-"+counter+" || SUCCESS: \"Create Rediffmail account\" webpage is opened");
    }

    // Find the total number of links in “Create Rediffmail account” webpage and print the links.
    static void findLinks() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        counter++;
        if (links.isEmpty()) {
            throw new RuntimeException("FAIL: No links found!");
        }
        System.out.println("TC-"+counter+" || SUCCESS: "+links.size()+" Links found: [");
        for (WebElement l : links) {
            String linkText = l.getDomAttribute("href");
            if(linkText.contains("http")){
                System.out.println(linkText);
            }
            System.out.println();
        }
        System.out.println("]");
    }

    // Click on "terms and conditions" link and wait for the child window to open
    static void navigateToChildWindow() {
        WebElement tnc;
        try {
            counter++;
            tnc = driver.findElement(By.linkText("terms and conditions"));
        } catch (Exception e) {
            throw new RuntimeException("ERROR: 'terms and conditions' element not found");
        }
        System.out.println("TC-"+counter+" || SUCCESS: 'terms and conditions' element found");
        // Switch to the child window
        Actions act = new Actions(driver);
        act.moveToElement(tnc).click().perform();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            counter++;
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Child window is not opened");
        }
        System.out.println("TC-"+counter+" || SUCCESS: Child window is opened");
    }

    // Validate the child window is opened, check its title, click OK, and return to parent window
    static void validateChildWindowIsOpen() {
        counter++;
        if (childWindow.isEmpty()) {
            throw new RuntimeException("TC-"+counter+" || FAIL: child window \"Terms and Conditions\" is not opened");
        }

        System.out.println("TC-"+counter+" || SUCCESS: child window \"Terms and Conditions\" is opened");
        driver.switchTo().window(childWindow);

        String expectedTitle = "Rediffmail: Terms and Conditions";
        counter++;
        if (!driver.getTitle().equals(expectedTitle)) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Child window title mismatched");
        }

        System.out.println("TC-"+counter+" || SUCCESS: Child window title is correct");

        // Click OK button in child window
        driver.findElement(By.xpath("//*[@value='OK']")).click();

        // Switch back to parent window
        try {
            counter++;
            driver.switchTo().window(parentWindow);
        } catch (Exception e) {
            throw new RuntimeException("TC-"+counter+" || FAIL: Unable to switch to parent window");
        }

        System.out.println("TC-"+counter+" || SUCCESS: Switched to parent window");
    }

    public static void main(String[] args) {
        // Step-by-step execution of the test flow
        try{
            driver = DriverSetup.getDriver();
            // Open base url
            navigateToBaseUrl();
            // Navigate to create account page
            navigateToAccountPage();
            // Verify whether the account page is open
            validateAccountPageIsOpen();
            // find all links
            findLinks();
            // open child window
            navigateToChildWindow();
            // Store window handles for switching
            parentWindow = driver.getWindowHandle();
            windowHandles = driver.getWindowHandles();
            List<String> handles = new ArrayList<>(windowHandles);
            childWindow = handles.get(1);
            // verify whether child window is open or not
            validateChildWindowIsOpen();
            // Close the browser
            DriverSetup.closeDriver(driver);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
