package com.volunteer.dutyroster.selenium;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility helper to capture screenshot evidence on test execution or failures.
 * Saves screenshots to the `target/screenshots/` directory.
 */
public class ScreenshotHelper {

    private static final String SCREENSHOT_DIR = "target/screenshots";

    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }

        try {
            Path dirPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = testName + "_" + timestamp + ".png";
            Path targetPath = dirPath.resolve(fileName);

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), targetPath);

            System.out.println("[SELENIUM] Screenshot captured: " + targetPath.toAbsolutePath());
            return targetPath.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("[SELENIUM] Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
