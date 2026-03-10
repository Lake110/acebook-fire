package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {

    WebDriver driver;
    Faker faker;
    String email;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        faker = new Faker();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        email = faker.name().username() + "@email.com";

        // Sign up and log in
        driver.get("http://localhost:8080/settings");
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        // Wait for Auth0 to complete and redirect back to the app
        wait.until(ExpectedConditions.urlContains("localhost:8080"));

        // Navigate to settings
        driver.get("http://localhost:8080/settings");

        // Wait for settings page to fully load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void settingsPageLoads() {
        assertTrue(driver.getTitle().contains("Acebook"));
    }

    @Test
    public void settingsPageShowsEmail() {
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(email));
    }

    @Test
    public void settingsPageShowsUsername() {
        String expectedUsername = email.substring(0, email.indexOf('@'));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(expectedUsername));
    }

    @Test
    public void userCanUpdateUsername() {
        String newUsername = faker.name().username();

        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys(newUsername);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/settings"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(newUsername));
    }

    @Test
    public void updatedUsernamePersistsOnPageReload() {
        String newUsername = faker.name().username();

        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys(newUsername);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/settings"));
        driver.get("http://localhost:8080/settings");

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(newUsername));
    }

}