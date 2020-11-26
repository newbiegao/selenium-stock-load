package com.kerongao.stock;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CVSFileDataServiceTest {

    private static Logger logger = LoggerFactory.getLogger(CVSFileDataServiceTest.class) ;

    @Autowired
    private CVSFileDataService cvsFileDataService ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService;

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;


    @AfterTestClass
    public void after(){

        seleniumStockDataService.closeWindow();
    }

    @Test
    public void loadStockDataToExcelFileTest(){

        Integer stockPeriodLimit = 8 ;
        seleniumStockDataService.openUrl(STOCK_URL);
        cvsFileDataService.loadStockDataToExcelFile(stockPeriodLimit);
    }

}
