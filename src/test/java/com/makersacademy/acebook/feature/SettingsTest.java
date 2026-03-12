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

        // Go to app - gets redirected to Auth0
        driver.get("http://localhost:8081/settings");

        // Wait for Auth0 signup link and click it
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='signup'], a[href*='sign-up'], [data-action='sign up']")
        ));
        driver.findElement(
                By.cssSelector("a[href*='signup'], a[href*='sign-up'], [data-action='sign up']")
        ).click();

        // Wait for signup form fields
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Debug: print where we land after signup
        wait.until(ExpectedConditions.urlContains("localhost:8081"));
        System.out.println("Redirected to: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());

        // Navigate to settings and wait for username input to confirm page loaded
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
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
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

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
        driver.get("http://localhost:8081/settings");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(bodyText.contains(newUsername));
    }
}