package com.kerongao.stock;

import com.kerongao.stock.model.YLBXStockTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class YLBXStockPageService {

    private static Logger logger = LoggerFactory.getLogger(YLBXStockPageService.class) ;

    @Autowired
    private WebDriver webDriver  ;

    public void openUrl( String url ){

        this.webDriver.get( url );
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> loadYLBXStockDataRowsWebElement(){

        List<WebElement> rowsWebElements = this.webDriver.findElements(By.cssSelector(".tb0td1 tbody tr")) ;

        return  rowsWebElements ;

    }

    public List<YLBXStockTable> loadYLBXStockDataRowsToDataModle(){

        List<WebElement> rowsWebElements = loadYLBXStockDataRowsWebElement() ;

        List<YLBXStockTable> ylbxStockTableList = new ArrayList<>() ;

        rowsWebElements.remove(0) ;

        rowsWebElements.forEach( webElement -> {

            logger.debug(" load and import YLBX data row : {} " , webElement.getText() );

            YLBXStockTable ylbxStockTable = new YLBXStockTable() ;
            ylbxStockTable.importRow( webElement );
            ylbxStockTableList.add(ylbxStockTable) ;

        } );

        return ylbxStockTableList ;

    }





}
