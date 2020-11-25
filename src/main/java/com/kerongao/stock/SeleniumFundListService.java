package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Service
public class SeleniumFundListService {

    @Autowired
    private WebDriver webDriver  ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService ;

    public void openUrl( String url )
    {
        this.webDriver.get( url );
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void closeWindow(){ this.webDriver.close(); this.webDriver.quit(); }

    public List<FundData> loadCurrentStockPageFundListToStockFundList(){

        List<FundData> fundDataList = new ArrayList<>() ;

        // get stock table rows and click navigate to fund page
        List<WebElement> currentStockTableRows  =  seleniumStockDataService.findCurrentStockTableRows() ;

        // get current windowHandle
        String originalWindow = webDriver.getWindowHandle();

        //
        for( WebElement trElement : currentStockTableRows ){

            // open a new window tab
            WebElement colElement =  trElement.findElement(By.className("col")) ;
            colElement.findElement(By.tagName("a")).click();

            // switchTo new window tab
            for( String windowHandle : webDriver.getWindowHandles()  )
            {
                if( !originalWindow.equalsIgnoreCase(windowHandle) )
                {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            // wait window load
            WebDriverWait webDriverWait = new WebDriverWait( this.seleniumStockDataService.getWebDriver() , Duration.ofSeconds(seleniumConfig.getPageLoadTimeOut()) );
            webDriverWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy( By.id("ccmx_table") , By.className("table-model") ));

            // add fund list
            findCurrenWindowFundListWelementTable(fundDataList) ;

            // close current window tab
            this.webDriver.close();
            this.webDriver.switchTo().window(originalWindow) ;

        }

        return fundDataList ;
    }

    /**
     * in current window page find fund list table and get fund into fundDataList
     * @param fundDataList
     */
    private void findCurrenWindowFundListWelementTable( List<FundData> fundDataList ){

          // find fund table list table
          List<WebElement> rowsList =  this.webDriver.findElement(By.id("ccmx_table"))
                    .findElement(By.className("table-model"))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("tr")) ;

          // add fund table list table rows to FundData List
          for( WebElement  rowElement :rowsList ){

              // cols : 0 序号  1 机构名称
              List<WebElement> colList = rowElement.findElements(By.tagName("td")) ;

              String fundName = colList.get(1).getText() ;
              String url = colList.get(1).findElement(By.tagName("a")).getAttribute("href") ;

              FundData fundData = new FundData() ;
              fundData.setName(fundName);
              fundData.setUrl(url);

              fundDataList.add(fundData) ;

          }

    }

}
