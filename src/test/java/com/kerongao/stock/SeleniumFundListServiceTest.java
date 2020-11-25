package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class SeleniumFundListServiceTest {

    private static String STOCK_URL = "http://data.eastmoney.com/zlsj/2020-09-30-3-2.html" ;

    @Autowired
    private SeleniumFundListService seleniumFundListService ;

    @Test
    public void navigateToStockFundListTest(){

        seleniumFundListService.openUrl(STOCK_URL);
        List<FundData> fundDataList = seleniumFundListService.loadCurrentStockPageFundListToStockFundList();

        for( FundData fundData : fundDataList ){
            System.out.println(fundData.getName() + "--" + fundData.getUrl() ) ;
        }

        assertThat(fundDataList ).isNotEmpty().describedAs(" can't fand stock fund data  ");
    }

    @AfterTestClass
    public void after(){

       //  seleniumFundListService.closeWindow();
    }
}
