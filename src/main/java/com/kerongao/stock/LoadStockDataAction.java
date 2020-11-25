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
    private SeleniumStockDataService seleniumStockDataService;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    public List<StockTable> loadStockAllPeriodDataByRange( int start  , int length ){

        List<String> options  = this.seleniumStockDataService.findStockPeriodSelectOptions() ;

        List<StockTable> stockTableList = new ArrayList<>() ;

        for( int i = 0 ; i < options.size() ; i++ ){

            // change stock period and load data
            this.seleniumStockDataService.doStockPeriodSelectAction(options.get(i));

            WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            if( i >= start && i < ( start + length ) ){
                //  paged load stock table data
                loadStockOnePeriodData( options.get(i) , stockTableList ) ;
            }
        }

        return stockTableList ;
    }

    /**
     * load stock data by period count limit
     * @param periodLimit  if periodLimit < 0 then load all period stock data
     * @return
     */
    public List<StockTable> loadStockAllPeriodData( int periodLimit ){

        List<String> options  = this.seleniumStockDataService.findStockPeriodSelectOptions() ;

        List<StockTable> stockTableList = new ArrayList<>() ;

        Integer periodCount = periodLimit > 0 ?  periodLimit : options.size() ;

        for( String option : options.subList(0 , periodCount ) )
        {
            // change stock period and load data
            this.seleniumStockDataService.doStockPeriodSelectAction(option);

            WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            //  paged load stock table data
            loadStockOnePeriodData( option , stockTableList ) ;
        }

        return stockTableList ;
    }

    public void loadStockOnePeriodData( String period  , List<StockTable> stockTableList ){

        // get first page stock data
        WebElement firstStockTable = seleniumStockDataService.findCurrentStockTable() ;
        addStockTableElementToDataModel(firstStockTable , stockTableList , period ) ;

        // next page stock data page
        WebElement nextPageElement = null ;
        // do next page action and get other stock page data

        while ( (nextPageElement =  seleniumStockDataService.findNextPageElement()) != null  )
        {
            seleniumStockDataService.nextPageClickAction( nextPageElement ) ;

            // wait stock data load
            WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            WebElement currentStockPage = seleniumStockDataService.findCurrentStockTable() ;
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
