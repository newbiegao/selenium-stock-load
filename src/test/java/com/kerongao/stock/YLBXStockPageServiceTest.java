package com.kerongao.stock;

import com.kerongao.stock.model.YLBXStockTable;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class YLBXStockPageServiceTest {

    @Autowired
    private YLBXStockPageService ylbxStockPageService ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    @Test
    public void  loadYLBXStockDataRowsWebElementTest(){

        ylbxStockPageService.getWebDriver().get(seleniumConfig.getYlbxStockPagePageUrl());

       List<WebElement> webElementList = ylbxStockPageService.loadYLBXStockDataRowsWebElement() ;

        webElementList.forEach( webElement -> {

            System.out.println(webElement.getText()) ;

        } );

       assertThat(webElementList.size()).isGreaterThan(2);

    }

    @Test
    public void loadYLBXStockDataRowsToDataModleTest(){

        ylbxStockPageService.getWebDriver().get(seleniumConfig.getYlbxStockPagePageUrl());

        List<YLBXStockTable> ylbxStockTableList = ylbxStockPageService.loadYLBXStockDataRowsToDataModle() ;

        AssertionsForClassTypes.assertThat(ylbxStockTableList.size()).isGreaterThan(2);

    }
}
