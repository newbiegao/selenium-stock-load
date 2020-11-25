package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class LoadFundDataListActionTest {

    private static  Logger logger = LoggerFactory.getLogger(LoadFundDataListActionTest.class) ;

    @Autowired
    private LoadFundDataListAction loadFundDataListAction ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService ;

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;

    @Test
    public void loadCurrentPeriodStockFundListTest(){

        logger.debug(" load page : {} " , STOCK_URL );

        seleniumStockDataService.openUrl(STOCK_URL);

       List<FundData> fundDataList =  loadFundDataListAction.loadCurrentPeriodStockFundList() ;

        fundDataList.forEach( fundData -> {

            System.out.println(fundData.getName() + "---" + fundData.getUrl()) ;

        } );

        assertThat(fundDataList).isNotEmpty() ;

    }

    @AfterTestClass
    public void after(){

        seleniumStockDataService.closeWindow();
    }

}
