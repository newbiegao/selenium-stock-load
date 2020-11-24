package com.kerongao.stock;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import java.time.Duration;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SeleniumServiceTest {

    @Autowired
    private SeleniumService seleniumService ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    private static String URL = "https://www.baidu.com" ;

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;


    @Test
    public void loadPageTest(){

       String html =  seleniumService.loadPageHtml(URL) ;
       assertThat( html ).isNotBlank().describedAs("www.baidu.com info html");

    }

    @Test
    public void loadStockPageHtmlTest(){

        String html = seleniumService.loadPageHtml(STOCK_URL) ;
        assertThat( html ).isNotBlank().describedAs(" load page html is blank ");
    }


    @AfterTestClass
    public void after(){

        seleniumService.closeWindow();
    }

    @Test
    public void configTest(){

       String pageUrl =  seleniumConfig.getStockStartPage() ;
       assertThat( pageUrl ).isEqualTo(STOCK_URL).describedAs(" stock start page = {}" , STOCK_URL) ;
    }

    @Test
    public void findCurrentStockTableTest(){

        seleniumService.openUrl(STOCK_URL);
        WebElement webElement =  seleniumService.findCurrentStockTable() ;

        System.out.println(webElement.getText()) ;

    }

    @Test
    public void findStockTableNextPageTest(){

        seleniumService.openUrl(STOCK_URL);
        WebElement webElement = seleniumService.findNextPageElement() ;

        assertThat(webElement.getText()).isEqualTo("下一页").describedAs( " can not find next stock page 下一页 info :{} " , webElement.getText() );
    }

    @Test
    public void clickNextPageActionTest() throws Exception {

        seleniumService.openUrl(STOCK_URL);

        WebElement webElement = null ;
        int page = -1 ;
        while ( (  webElement = seleniumService.findNextPageElement()) != null  )
        {
            page =  seleniumService.nextPageClickAction(webElement);
             // wait page load stock data
            WebDriverWait webDriverWait = new WebDriverWait( seleniumService.getWebDriver() , Duration.ofSeconds(5) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("default_table"))) ;

            System.out.println( " current page index = " + page );

        }

        assertThat( page ).isGreaterThan( 0 ).describedAs( " do next page aciton error : {} " , page ) ;

    }

    @Test
    public void findStockPeriodSelectTest(){

        seleniumService.openUrl(STOCK_URL);

        List<String> options = this.seleniumService.findStockPeriodSelectOptions() ;

        options.forEach( option -> {
            System.out.println( option ) ;
        } );

        assertThat(options).isNotEmpty().describedAs(" can't find stock period select options  ") ;

    }

    @Test
    public void doStockPeriodSelectActionTest(){

        seleniumService.openUrl(STOCK_URL);
        List<String> options  = this.seleniumService.findStockPeriodSelectOptions() ;

        for( String option : options )
        {
            System.out.println(" do action data : " + option );
            this.seleniumService.doStockPeriodSelectAction(option);

            WebDriverWait webDriverWait = new WebDriverWait( seleniumService.getWebDriver() , Duration.ofSeconds(5) );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("default_table"))) ;

        }

        assertThat(options).isNotEmpty().describedAs(" can't find select options ") ;
    }

}
