package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
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
public class LoadFundDataListAction {

    private static Logger logger = LoggerFactory.getLogger(LoadFundDataListAction.class) ;

    @Autowired
    private SeleniumFundListService seleniumFundListService ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    public List<FundData> loadCurrentPeriodStockFundList(){

        List<FundData> fundDataList = new ArrayList<>() ;

        // load first page data
        logger.debug(" start process stock table page : 0  , find fund name list ");
        seleniumFundListService.loadCurrentStockPageFundListToStockFundList(fundDataList) ;

        // next page stock data page
        WebElement nextPageElement = null ;

        int pageIndex = 0 ;
        while ( (nextPageElement =  seleniumStockDataService.findNextPageElement()) != null  )
        {
            pageIndex++ ;

            // do next page click action
            seleniumStockDataService.nextPageClickAction( nextPageElement ) ;

            // wait stock data load
          //  WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
         //   webDriverWait.until( ExpectedConditions.visibilityOfNestedElementsLocatedBy( By.id(seleniumConfig.getStockTableDivID()) , By.className(seleniumConfig.getStockTableClass()) ));
            WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , 60 );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(
                    this.seleniumStockDataService.getWebDriver().findElement(By.id(seleniumConfig.getStockTableDivID()))
                    .findElement(By.className(seleniumConfig.getStockTableClass()))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("tr")).get(0)
                    .findElements(By.tagName("td")).get(4)
            )) ;
            logger.debug(" start process stock table page : {} , find fund name list " , pageIndex);
            seleniumFundListService.loadCurrentStockPageFundListToStockFundList(fundDataList) ;

        }

        return fundDataList ;

    }

}
