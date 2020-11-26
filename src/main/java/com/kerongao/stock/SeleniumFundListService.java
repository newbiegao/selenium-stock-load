package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Service
public class SeleniumFundListService {

    private static Logger logger = LoggerFactory.getLogger(SeleniumFundListService.class) ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService ;


    /**
     * load current stock page data and open each fund link page to get fund list
     * @return
     */
    public void loadCurrentStockPageFundListToStockFundList( List<FundData> fundDataList ){

        // get stock table rows and click navigate to fund page
        List<WebElement> currentStockTableRows  =  seleniumStockDataService.findCurrentStockTableRows() ;

        // get current windowHandle
        String originalWindow = this.seleniumStockDataService.getWebDriver().getWindowHandle();

        int rowIndex = 0 ;
        //
        for( WebElement trElement : currentStockTableRows ){

            rowIndex++ ;

            // find click <a>
            WebElement clickElement =  trElement.findElement(By.className("col")).findElement(By.tagName("a")) ;

           //  WebDriverWait clickWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(5) );
            WebDriverWait clickWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , 30 );
            clickWait.until( ExpectedConditions.elementToBeClickable(clickElement) ) ;
            // open a new window tab
            clickElement.click();

            // waite window open and load
           //  WebDriverWait windowWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(5) );
            WebDriverWait windowWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , 5 );
            windowWait.until( ExpectedConditions.numberOfWindowsToBe(2));

            // switchTo new window tab
            for( String windowHandle : this.seleniumStockDataService.getWebDriver().getWindowHandles()  )
            {
                if( !originalWindow.equalsIgnoreCase(windowHandle) )
                {
                    this.seleniumStockDataService.getWebDriver().switchTo().window(windowHandle);
                    logger.debug(" window switch from {} to {} " , originalWindow , windowHandle);
                    break;
                }
            }

            // wait window load
           //  WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(30) );
            WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , 30 );
            webDriverWait.until(
                    ExpectedConditions.presenceOfNestedElementLocatedBy(By.id("ccmx_table") , By.className("table-model"))
            ) ;
            // add fund list
            logger.debug(" start process stock table row : {} , find fund name list " , rowIndex);
            findCurrenWindowFundListWeblementTable(fundDataList) ;

            // close current window tab
            this.seleniumStockDataService.getWebDriver().close();
            this.seleniumStockDataService.getWebDriver().switchTo().window(originalWindow) ;

        }

    }

    /**
     * in current window page find fund list table and get fund into fundDataList
     * @param fundDataList
     */
    private void findCurrenWindowFundListWeblementTable( List<FundData> fundDataList ){

          // find fund table list table
          List<WebElement> rowsList =  this.seleniumStockDataService.getWebDriver().findElement(By.id("ccmx_table"))
                    .findElement(By.className("table-model"))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("tr")) ;

          // add fund table list table rows to FundData List
          for( WebElement  rowElement :rowsList ){

              logger.debug(" start load fund name and url from fund list table : {} " , rowElement.getText() );

              // cols : 0 序号  1 机构名称
              List<WebElement> colList = rowElement.findElements(By.tagName("td")) ;

              if( colList.size() < 2 )
              {
                  logger.error(" load fund name and url from fund list table error innerHtml is : { } " , rowElement.getText() );
                  continue;
              }

              String fundName = colList.get(1).getText() ;
              String url = colList.get(1).findElement(By.tagName("a")).getAttribute("href") ;

              FundData fundData = new FundData() ;
              fundData.setName(fundName);
              fundData.setUrl(url);

              fundDataList.add(fundData) ;

          }

    }

}
