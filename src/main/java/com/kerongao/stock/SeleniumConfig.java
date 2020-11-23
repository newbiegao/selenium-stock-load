package com.kerongao.stock;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SeleniumConfig {

    public String getStockStartPage() {
        return stockStartPage;
    }

    public void setStockStartPage(String stockStartPage) {
        this.stockStartPage = stockStartPage;
    }

    @Value("${html.stockStartPage.url}")
    private String stockStartPage  ;

    @Value("${html.stockTableClass}")
    private String stockTableClass ;

    @Value("${html.stockTableDivID}")
    private String stockTableDivID ;

    @Value("${html.stockNextPageClass}")
    private String stockNextPageClass  ;

    @Value("${html.stockNextPageText}")
    private String stockNextPageText  ;

    public String getStockNextPageText() {
        return stockNextPageText;
    }

    public void setStockNextPageText(String stockNextPageText) {
        this.stockNextPageText = stockNextPageText;
    }

    public String getStockNextPageClass() {
        return stockNextPageClass;
    }

    public void setStockNextPageClass(String stockNextPageClass) {
        this.stockNextPageClass = stockNextPageClass;
    }

    public String getStockTableDivID() {
        return stockTableDivID;
    }

    public void setStockTableDivID(String stockTableDivID) {
        this.stockTableDivID = stockTableDivID;
    }

    public String getStockTableClass() {
        return stockTableClass;
    }

    public void setStockTableClass(String stockTableClass) {
        this.stockTableClass = stockTableClass;
    }

    @Bean
    public WebDriver webDriverFactory(){

        System.setProperty("webdriver.gecko.driver", "./bin/geckodriver");
        System.setProperty("webdriver.chrome.driver", "./bin/chromedriver");

        FirefoxOptions firefoxOptions = new FirefoxOptions() ;
        firefoxOptions.setLogLevel( FirefoxDriverLogLevel.WARN ) ;
        firefoxOptions.setPageLoadStrategy( PageLoadStrategy.NORMAL ) ;

        WebDriver webDriver = new FirefoxDriver(firefoxOptions) ;

        // webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

        return webDriver ;
    }

}
