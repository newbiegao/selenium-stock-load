package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoadFundDataListAction {

    private static Logger logger = LoggerFactory.getLogger(LoadFundDataListAction.class) ;

    @Autowired
    private SeleniumFundListService seleniumFundListService ;

    @Autowired
    private SeleniumStockDataService seleniumStockDataService ;

    @Autowired
    private SeleniumConfig seleniumConfig ;

    public List<FundData> loadCurrentPeriodStockFundList(){

        List<FundData> fundDataList = new ArrayList<>() ;

        Integer pageIndex = 1 ;

        // load first page data
        logger.debug(" start process stock table page : {}  , find fund name list " , pageIndex );
        seleniumFundListService.loadCurrentStockPageFundListToStockFundList(fundDataList) ;

        // next page stock data page
        WebElement nextPageElement = null ;

        while ( (pageIndex =  seleniumStockDataService.doNextPageClickAction()) > 0  )
        {
            pageIndex++ ;

            // load current stock table page rows fund list
            seleniumFundListService.loadCurrentStockPageFundListToStockFundList(fundDataList) ;

        }

        return fundDataList ;

    }

}
