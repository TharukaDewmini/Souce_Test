package SauceDemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class NegativeTest {

    WebDriver driver;

    @BeforeClass
    public void setup() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
    }

    //  TC - Invalid Login
    @Test(priority = 1)
    public void invalidLoginTest() {

        driver.findElement(By.id("user-name")).sendKeys("wrong_user");
        driver.findElement(By.id("password")).sendKeys("wrong_pass");
        driver.findElement(By.id("login-button")).click();

        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));

        Assert.assertTrue(
                error.getText().contains("Username and password do not match"),
                "Invalid login error not displayed"
        );

        // refresh back to login page for next tests
        driver.navigate().refresh();
    }

    //  TC - Empty Cart Checkout
    @Test(priority = 2)
    public void emptyCartCheckout() {

        // LOGIN FIRST (required for this flow)
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.className("shopping_cart_link")).click();

        boolean cartEmpty = driver.findElements(By.className("cart_item")).isEmpty();

        Assert.assertTrue(cartEmpty, "Cart should be empty for this test");
    }

    // TC - Missing Information Validation
    @Test(priority = 3)
    public void missingInformationValidation() {

        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("continue")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("error-message-container"))
        );

        Assert.assertTrue(errorMsg.getText().contains("Error"));
    }


    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
