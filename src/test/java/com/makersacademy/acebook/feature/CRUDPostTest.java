package com.makersacademy.acebook.feature;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class CRUDPostTest {

    WebDriver driver;
    Faker faker;

    @BeforeEach
    public void login() {
        driver = new ChromeDriver();
        faker = new Faker();
        String email = faker.name().username() + "@email.com";
        driver.get("http://localhost:8081/posts");
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void testCreatePost() {
        // Navigate to the posts page
        WebElement contentInput = driver.findElement(By.name("content"));
        contentInput.sendKeys("This is a test");
        driver.findElement(By.cssSelector("input[type='submit'][value='Submit']")).click();

        driver.get("http://localhost:8081");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement newPost = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='posts']//div[@class='post-main']/p[text()='This is a test']"))
        );
        Assertions.assertTrue(newPost.isDisplayed(), "The test post should be displayed on the page");
        }

    @Test
    public void testDelete() {
        WebElement contentInput = driver.findElement(By.name("content"));
        contentInput.sendKeys("Entry to be deleted");
        driver.findElement(By.cssSelector("input[type='submit'][value='Submit']")).click();
        driver.get("http://localhost:8081");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the delete button of the post with the exact text
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='post-main'][p[text()='Entry to be deleted']]//button[text()='Delete']")
        ));
        deleteButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@class='posts']//div[@class='post-main']/p[text()='Entry to be deleted']")
        ));
        Assertions.assertTrue(driver.findElements(By.xpath(
                "//div[@class='posts']//div[@class='post-main']/p[text()='Entry to be deleted']"
        )).isEmpty(), "The post should no longer exist after deletion");
    }


}