package org.example.stockPrice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class StockPriceValidator {

    public static void main(String[] args) {
        WebDriver driver = initializeWebDriver();
        HashMap<String, Double> excelData = readExcelData();
        HashMap<String, Double> webData = readWebData(driver);

        compareData(excelData, webData);

        driver.quit();
    }

    private static WebDriver initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Set the path to your chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode (no UI)
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://money.rediff.com/losers/bse/daily/groupall");
        return driver;
    }

    private static HashMap<String, Double> readExcelData() {
        HashMap<String, Double> excelData = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(new File("path/to/your/data.xlsx")); // Set the path to your Excel file
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String company = row.getCell(0).getStringCellValue();
                double currentPrice = row.getCell(3).getNumericCellValue();
                excelData.put(company, currentPrice);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelData;
    }

    private static HashMap<String, Double> readWebData(WebDriver driver) {
        HashMap<String, Double> webData = new HashMap<>();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Wait for the table to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("leftcontainer")));

        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='dataTable']/tbody/tr"));

        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));

            String company = columns.get(0).getText();
            double currentPrice = Double.parseDouble(columns.get(3).getText());

            webData.put(company, currentPrice);
        }

        return webData;
    }

    private static void compareData(HashMap<String, Double> excelData, HashMap<String, Double> webData) {
        for (String company : excelData.keySet()) {
            if (webData.containsKey(company)) {
                double excelPrice = excelData.get(company);
                double webPrice = webData.get(company);

                if (excelPrice != webPrice) {
                    System.out.println("Mismatch for company: " + company);
                    System.out.println("Excel Price: " + excelPrice);
                    System.out.println("Web Price: " + webPrice);
                    System.out.println();
                }
            } else {
                System.out.println("Company not found on the website: " + company);
                System.out.println();
            }
        }
    }
}

