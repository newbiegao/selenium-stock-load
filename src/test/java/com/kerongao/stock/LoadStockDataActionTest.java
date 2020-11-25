package com.kerongao.stock;

import com.kerongao.stock.model.StockTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LoadStockDataActionTest {

    @Autowired
    private LoadStockDataAction loadStockDataAction ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService;

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;

    @AfterTestClass
    public void after(){

        seleniumStockDataService.closeWindow();
    }

    @Test
    public void  loadStockAllPeriodDataByRangeTest(){

        int start = 2 ;
        int length = 2 ;

        seleniumStockDataService.openUrl(STOCK_URL);
        List<StockTable> stockTableList = loadStockDataAction.loadStockAllPeriodDataByRange(start,length) ;
        stockTableList.forEach( stockTable -> {
            System.out.println(stockTable.getElementText()) ;
        } );

        assertThat( stockTableList ).isNotEmpty().describedAs("can't load stock data") ;
    }

    @Test
    public void loadStockAllPeriodDataTest (){

        int periodLimit = -1 ;

        seleniumStockDataService.openUrl(STOCK_URL);
        List<StockTable> stockTableList = loadStockDataAction.loadStockAllPeriodData(periodLimit) ;

        stockTableList.forEach( stockTable -> {
            System.out.println(stockTable.getElementText()) ;
        } );

        assertThat( stockTableList ).isNotEmpty().describedAs("can't load stock data") ;
    }

    @Test
    public void loadStockOnePeriodDataTest(){

        seleniumStockDataService.openUrl(STOCK_URL);

        List<StockTable> stockTableList = new ArrayList<>() ;

       loadStockDataAction.loadStockOnePeriodData("2020-09-30" , stockTableList  ) ;

       stockTableList.forEach( stockTable -> {
           System.out.println(stockTable.getElementText()) ;
       } );

        assertThat(stockTableList).isNotEmpty().describedAs( " can't load stock data  " ) ;

    }

}
