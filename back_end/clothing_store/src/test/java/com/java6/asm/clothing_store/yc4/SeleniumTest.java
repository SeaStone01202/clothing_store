package com.java6.asm.clothing_store.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class UntitledTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\haith\\Downloads\\edgedriver_win64\\msedgedriver.exe");
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Khởi tạo wait với timeout 10 giây
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testLogin() {
        driver.get("http://localhost:5173/");
        driver.manage().window().setSize(new Dimension(1343, 721));
        driver.findElement(By.id("accountDropdown")).click();
        driver.findElement(By.linkText("🔑 Đăng nhập")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(1) > .form-control")).sendKeys("admin@gmail.com");
        driver.findElement(By.cssSelector(".mb-3:nth-child(2) > .form-control")).click();
        driver.findElement(By.cssSelector(".mb-3:nth-child(2) > .form-control")).sendKeys("admin");
        driver.findElement(By.cssSelector(".btn-primary")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".btn-primary"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.textToBe(By.id("accountDropdown"), "Nguyễn Văn Admin"));
        }
    }
}