package com.makersacademy.acebook.feature;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavbarTest {

    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void homePageShowsNavbarLinksForLoggedOutUser() {
        driver.get("http://localhost:8080/");

        assertTrue(driver.findElement(By.linkText("Feed")).isDisplayed());
        assertTrue(driver.findElement(By.linkText("Profile")).isDisplayed());
        assertTrue(driver.findElement(By.linkText("Login")).isDisplayed());
    }
}
