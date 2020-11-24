package com.kerongao.stock;

import com.kerongao.stock.model.StockTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoadStockDataAction {

    @Autowired
    private SeleniumService seleniumService ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    public List<StockTable> loadStockAllPeriodData( int limit ){

        List<String> options  = this.seleniumService.findStockPeriodSelectOptions() ;

        List<StockTable> stockTableList = new ArrayList<>() ;

        for( String option : options.subList(0 , limit ) )
        {
            // change stock period and load data
            this.seleniumService.doStockPeriodSelectAction(option);

            WebDriverWait webDriverWait = new WebDriverWait( seleniumService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            //  paged load stock table data
            loadStockOnePeriodData( option , stockTableList ) ;
        }

        return stockTableList ;
    }

    public void loadStockOnePeriodData( String period  , List<StockTable> stockTableList ){

        // get first page stock data
        WebElement firstStockTable = seleniumService.findCurrentStockTable() ;
        addStockTableElementToDataModel(firstStockTable , stockTableList , period ) ;

        // next page stock data page
        WebElement nextPageElement = null ;
        // do next page action and get other stock page data

        while ( (nextPageElement =  seleniumService.findNextPageElement()) != null  )
        {
            seleniumService.nextPageClickAction( nextPageElement ) ;

            // wait stock data load
            WebDriverWait webDriverWait = new WebDriverWait( this.seleniumService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            WebElement currentStockPage = seleniumService.findCurrentStockTable() ;
            addStockTableElementToDataModel(currentStockPage , stockTableList , period ) ;
        }

    }

    /**
     *  transfer stock data table weelement to data model
     * @param webElement
     * @param stockTableList
     * @param period
     */
    private void addStockTableElementToDataModel(WebElement webElement , List<StockTable> stockTableList , String period ){

        WebElement tbody  = webElement.findElement(By.tagName("tbody")) ;
        List<WebElement> trList = tbody.findElements(By.tagName("tr")) ;

        for( WebElement element : trList ){
            StockTable stockTable = new StockTable() ;
            stockTable.setPeriod(period);
            stockTable.importWebElement(element) ;
            stockTableList.add(stockTable) ;
        }
    }

}
