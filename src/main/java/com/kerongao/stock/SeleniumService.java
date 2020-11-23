package com.kerongao.stock;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeleniumService {

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

    /**
    static {

        System.setProperty("webdriver.gecko.driver", "/bin/geckodriver");
        System.setProperty("webdriver.chrome.driver", "/bin/chromedriver");

    }
    **/
    public String loadPageHtml( String url  ){

        webDriver.get(url);
        return webDriver.getPageSource() ;
    }

    public void openUrl( String url )
    {
        this.webDriver.get( url );
    }

    public WebElement findCurrentStockTable( ){

       WebElement webElement = this.webDriver.findElement(By.id(seleniumConfig.getStockTableDivID()))
               .findElement(By.className(seleniumConfig.getStockTableClass())) ;
       return webElement ;
    }

    public WebElement findNextPageElement(){

        List<WebElement> webElements = this.webDriver.findElement(By.className(seleniumConfig.getStockNextPageClass()))
                .findElements(By.tagName("a")) ;

        webElements.removeIf( webElement -> {

            if( webElement.getText().equals(seleniumConfig.getStockNextPageText()) )
            {
                return false ;
            }
            else{
                return true ;
            }
        }  ) ;

        if( webElements.isEmpty() ) {return  null  ;}
        return webElements.get(0) ;
    }

    /**
     *  do next page action
     * @param nextPageElement
     * @return next page index , if no page find return -1
     */
    public int  nextPageClickAction( WebElement nextPageElement ){

        nextPageElement.click();

        // find current page
        WebElement webElement = this.webDriver.findElement(By.className(seleniumConfig.getStockNextPageClass()))
                .findElement(By.className("active")) ;

        if ( webElement == null ) { return  -1 ; }

        return  Integer.valueOf(webElement.getText()) ;
    }

    public void closeWindow() {

        this.webDriver.close();
        this.webDriver.quit();

    }

    public List<String> findStockPeriodSelectOptions(){

        WebElement selectElement = this.webDriver.findElement(By.className("slt")) ;
        List<WebElement> optionElements = selectElement.findElements(By.tagName("option")) ;
        List<String> options = new ArrayList<>() ;
        for( WebElement webElement : optionElements ){
            options.add( webElement.getText() );
        }

        return options ;
    }

    public void doStockPeriodSelectAction( String period ){

        WebElement selectElement = this.webDriver.findElement(By.className("slt")) ;
         new Select(selectElement).selectByValue(period);

    }



}
