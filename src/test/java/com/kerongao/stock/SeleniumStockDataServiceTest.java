package com.kerongao.stock;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import java.time.Duration;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SeleniumStockDataServiceTest {

    private static Logger logger = LoggerFactory.getLogger(SeleniumStockDataServiceTest.class) ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    private static String URL = "https://www.baidu.com" ;

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;


    @Test
    public void loadPageTest(){

       String html =  seleniumStockDataService.loadPageHtml(URL) ;
       assertThat( html ).as(html).isNotBlank().describedAs("www.baidu.com info html");

    }

    @Test
    public void loadStockPageHtmlTest(){

        String html = seleniumStockDataService.loadPageHtml(STOCK_URL) ;
        assertThat( html ).isNotBlank().describedAs(" load page html is blank ");
    }


    @AfterTestClass
    public void after(){

        seleniumStockDataService.closeWindow();
    }

    @Test
    public void configTest(){

       String pageUrl =  seleniumConfig.getStockStartPage() ;
       assertThat( pageUrl ).isEqualTo(STOCK_URL).describedAs(" stock start page = {}" , STOCK_URL) ;
    }

    @Test
    public void findCurrentStockTableTest(){

        seleniumStockDataService.openUrl(STOCK_URL);
        WebElement webElement =  seleniumStockDataService.findCurrentStockTable() ;

        logger.debug(" current stock table html : {} " , webElement.getText() );

        assertThat( webElement ).isNotNull().describedAs(" can't find stock table webElement ") ;

    }

    @Test
    public void doNextPageClickActionTest()  {

        seleniumStockDataService.openUrl(STOCK_URL);

        Integer currentPage = 0 ;
        while( (  currentPage = seleniumStockDataService.doNextPageClickAction()) > 0  ){
            logger.debug(" current loaded page : {} " , currentPage );
        }

        assertThat( currentPage ).isLessThan(0).describedAs( " do next page aciton error ") ;

    }

    @Test
    public void findStockPeriodSelectTest(){

        seleniumStockDataService.openUrl(STOCK_URL);

        List<String> options = this.seleniumStockDataService.findStockPeriodSelectOptions() ;

        options.forEach( option -> {
            System.out.println( option ) ;
        } );

        assertThat(options).isNotEmpty().describedAs(" can't find stock period select options ") ;

    }

    @Test
    public void doStockPeriodSelectActionTest(){

        seleniumStockDataService.openUrl(STOCK_URL);
        List<String> options  = this.seleniumStockDataService.findStockPeriodSelectOptions() ;

        for( String option : options )
        {
            System.out.println(" do action data : " + option );
            this.seleniumStockDataService.doStockPeriodSelectAction(option);

            WebDriverWait webDriverWait = new WebDriverWait( seleniumStockDataService.getWebDriver() , 5 );
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("default_table"))) ;

        }

        assertThat(options).isNotEmpty().describedAs(" can't find select options ") ;
    }

}
