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

    public class ProfileTest {

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
            driver.get("http://localhost:8081/profile");
            driver.findElement(By.linkText("Sign up")).click();
            driver.findElement(By.name("email")).sendKeys(email);
            driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
            driver.findElement(By.name("action")).click();

            // Wait for Auth0 to redirect back
            wait.until(ExpectedConditions.urlContains("localhost:8081"));

            // Navigate to profile
            driver.get("http://localhost:8081/profile");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }

        @AfterEach
        public void tearDown() {
            driver.quit();
        }

        @Test
        public void profilePageLoads() {
            assertTrue(driver.getTitle().contains("Profile"));
        }

        @Test
        public void profilePageShowsEmail() {
            String bodyText = driver.findElement(By.tagName("body")).getText();
            assertTrue(bodyText.contains(email));
        }

        @Test
        public void profilePageShowsUsername() {
            String expectedUsername = email.substring(0, email.indexOf('@'));
            String bodyText = driver.findElement(By.tagName("body")).getText();
            assertTrue(bodyText.contains(expectedUsername));
        }

    }

