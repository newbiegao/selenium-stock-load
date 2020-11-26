package com.kerongao.stock;

import com.kerongao.stock.model.StockTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoadStockDataAction {

    private static Logger logger = LoggerFactory.getLogger(LoadStockDataAction.class) ;

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

          //  WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , 5 );
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

           //  WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , 5 );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(seleniumConfig.getStockTableDivID()))) ;

            //  paged load stock table data
            loadStockOnePeriodData( option , stockTableList ) ;
        }

        return stockTableList ;
    }

    /**
     * load one period all stock data , and paged load all stock data in period
     * @param period
     * @param stockTableList
     */
    public void loadStockOnePeriodData( String period  , List<StockTable> stockTableList ){

        logger.debug(" start load stocks period : {} " , period );

        // get first page stock data
        List<WebElement>  firstStockTableRows = seleniumStockDataService.findCurrentStockTableRows() ;
        addStockTableElementToDataModel(firstStockTableRows , stockTableList , period ) ;

        logger.debug(" load stocks period : {} , page : 0 , stocks : {}  " ,period , firstStockTableRows.size() );

        // next page stock data page
        WebElement nextPageElement = null ;

        Integer pageCount  = 0 ;

        // do next page action and get other stock page data
        while ( (nextPageElement =  seleniumStockDataService.findNextPageElement()) != null  )
        {
            pageCount ++ ;

            // do next page and wait page loaded
            seleniumStockDataService.nextPageClickAction( nextPageElement ) ;

            List<WebElement> currentStockTableRows = seleniumStockDataService.findCurrentStockTableRows() ;
            addStockTableElementToDataModel(currentStockTableRows , stockTableList , period ) ;

            logger.debug(" load stocks period : {} , page : {}  , stocks : {}  " , period ,pageCount ,  currentStockTableRows.size() );
        }

    }

    /**
     * transfer stock data table weelement to data model
     * @param currentStockTableRows
     * @param stockTableList
     * @param period
     */
    private void addStockTableElementToDataModel(List<WebElement> currentStockTableRows , List<StockTable> stockTableList , String period ){

        for( WebElement element : currentStockTableRows ){
            StockTable stockTable = new StockTable() ;
            stockTable.setPeriod(period);
            stockTable.importWebElement(element) ;
            stockTableList.add(stockTable) ;
        }
    }

}
