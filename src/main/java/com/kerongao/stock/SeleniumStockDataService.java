package com.kerongao.stock;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeleniumStockDataService {

    private static Logger logger = LoggerFactory.getLogger(SeleniumStockDataService.class) ;

    @Autowired
    private WebDriver webDriver  ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String loadPageHtml(String url  ){

        webDriver.get(url);
        return webDriver.getPageSource() ;
    }

    public void openUrl( String url )
    {
        this.webDriver.get( url );
    }

    public List<WebElement> findCurrentStockTableRows(){

        List<WebElement> trList = this.webDriver.findElements(By.cssSelector("div[id='default_table'] > table > tbody > tr")) ;
        return trList ;
    }

    public WebElement findCurrentStockTable( ){

        WebElement webElement = this.webDriver.findElement(By.cssSelector("div[id='default_table'] > table")) ;
        return webElement ;
    }

    /**
     * do next page action
     * @return next page Index
     */
    public int doNextPageClickAction(){

        // find last <a> element maybe [last page] or [next page]
        WebElement nextPageWebElement = this.webDriver.findElement(By.cssSelector("div[id='default_table_pager'] > div > a:last-child")) ;
        if( nextPageWebElement.getText().equalsIgnoreCase("下一页") ) {

            // do next page action
            nextPageWebElement.click();

            // wait page load finished
            WebDriverWait webDriverWait = new WebDriverWait( this.webDriver , 60 );
           // webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[id='default_table'] > table > tbody > tr:nth-child(1) > td:nth-child(5) > a"))) ;
            webDriverWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div[id='default_table'] > table > tbody > tr:nth-child(1) > td") , 1 )) ;

            // after next page and return current page
            WebElement currentPageWebElement = this.webDriver.findElement(By.cssSelector("div[id='default_table_pager'] > div > a[class='active']")) ;
            return Integer.valueOf(currentPageWebElement.getText()) ;
        }
        else {
            return -1 ;
        }
    }

    public void closeWindow() {

        this.webDriver.close();
        this.webDriver.quit();

    }

    public List<String> findStockPeriodSelectOptions(){

        // wait page load finished
        WebDriverWait webDriverWait = new WebDriverWait( this.webDriver , 30 );
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[id='default_table'] > table > tbody > tr:nth-child(1) > td:nth-child(5) > a"))) ;

        WebElement selectElement = this.webDriver.findElement(By.className("slt")) ;
        List<WebElement> optionElements = selectElement.findElements(By.tagName("option")) ;
        List<String> options = new ArrayList<>() ;
        for( WebElement webElement : optionElements ){
            options.add( webElement.getText() );
        }

        return options ;
    }

    /**
     * do stock period selection navigate page to next period
     * @param period
     */
    public void doStockPeriodSelectAction( String period ){

        WebDriverWait webDriverWait = new WebDriverWait( this.webDriver , 30 );
        webDriverWait.until( ExpectedConditions.elementToBeClickable(By.className("slt")) ) ;

        WebElement selectElement = this.webDriver.findElement(By.className("slt")) ;
        new Select(selectElement).selectByValue(period);

        // wait page page load
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[id='default_table'] > table > tbody > tr:nth-child(1) > td:nth-child(5) > a"))) ;
    }

}
