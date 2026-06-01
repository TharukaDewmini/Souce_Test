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

public class SauceDemoTest {
    WebDriver driver;
    String baseURL = "https://www.saucedemo.com/";

    @BeforeClass
    public void setup() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseURL);

    }

    // TC01 - Login Test
    @Test(priority = 1)
    public void verifyLogin() {

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Products");

    }

    // TC02 - Pop-up manual

    // TC03 - Burger Menu
    @Test(priority = 3)
    public void verifyBurgerMenu() {

        // Wait for you to manually close the popup after login
        System.out.println("Please manually close the popup. Waiting 10 seconds...");
        try {
            Thread.sleep(10000); // wait 10 seconds, adjust if needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        driver.findElement(By.id("react-burger-menu-btn")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));
        Assert.assertTrue(logout.isDisplayed());


        driver.findElement(By.id("react-burger-cross-btn")).click();
    }

    // TC04 - Add Product To Cart
    @Test(priority = 4)
    public void addProductToCart() {


        try {
            WebElement closeBurger = driver.findElement(By.id("react-burger-cross-btn"));
            if (closeBurger.isDisplayed()) {
                closeBurger.click();
            }
        } catch (Exception e) {

        }


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-sauce-labs-backpack")
        ));
        addToCartBtn.click();


        WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("remove-sauce-labs-backpack")
        ));

        Assert.assertTrue(removeBtn.isDisplayed(), "Product was not added to cart!");

        System.out.println("Product added to cart successfully.");
    }

    // TC05 - Open Cart Page
    @Test(priority = 5)
    public void openCartPage() {

        driver.findElement(By.className("shopping_cart_link")).click();

        String cartTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(cartTitle, "Your Cart");

    }



    // TC06 - Checkout Process
    @Test(priority = 6)
    public void checkoutProcess() {

        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Tharuka");
        driver.findElement(By.id("last-name")).sendKeys("Dewmini");
        driver.findElement(By.id("postal-code")).sendKeys("12345");

        driver.findElement(By.id("continue")).click();

        String overview = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(overview, "Checkout: Overview");

    }

    // TC07 - Total Price Verification
    @Test(priority = 7)
    public void verifyTotalPrice() {

        String itemTotal = driver.findElement(By.className("summary_subtotal_label")).getText();
        String tax = driver.findElement(By.className("summary_tax_label")).getText();
        String total = driver.findElement(By.className("summary_total_label")).getText();

        System.out.println(itemTotal);
        System.out.println(tax);
        System.out.println(total);

    }

    // TC08 - Finish Order
    @Test(priority = 8)
    public void finishOrder() {

        driver.findElement(By.id("finish")).click();

        String message = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(message, "Thank you for your order!");

    }


    // TC09 - Back Home
    @Test(priority = 9, dependsOnMethods = {"finishOrder"})
    public void backHome() {
        System.out.println("Executing TC09 - Back Home");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement backHomeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("back-to-products")));
        backHomeBtn.click();

        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Products");
    }



    // TC10 - Logout
    @Test(priority = 10)
    public void logoutFunction() throws InterruptedException {


        driver.findElement(By.id("react-burger-menu-btn")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("logout_sidebar_link")).click();

        boolean loginPage = driver.findElement(By.id("login-button")).isDisplayed();

        Assert.assertTrue(loginPage, "User is not redirected to Login Page");

    }

    @AfterClass
    public void closeBrowser() {

        driver.quit();

    }

}