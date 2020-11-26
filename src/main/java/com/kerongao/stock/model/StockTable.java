package com.kerongao.stock.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StockTable {

    private String period ;

    private String reportTime ;

    private String stockCode ;

    private String stockName ;

    private Integer financialCount ;

    private Double stockCount ;

    private Double stockRate ;

    private String upDown ;

    private Double upDownCount ;

    private Double upDownRate ;

    private String elementText ;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getStockCount() {
        return stockCount;
    }

    public void setStockCount(Double stockCount) {
        this.stockCount = stockCount;
    }

    public Double getUpDownCount() {
        return upDownCount;
    }

    public void setUpDownCount(Double upDownCount) {
        this.upDownCount = upDownCount;
    }

    public String getElementText() {
        return elementText;
    }

    public void setElementText(String elementText) {
        this.elementText = elementText;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getFinancialCount() {
        return financialCount;
    }

    public void setFinancialCount(Integer financialCount) {
        this.financialCount = financialCount;
    }

    public Double getStockRate() {
        return stockRate;
    }

    public void setStockRate(Double stockRate) {
        this.stockRate = stockRate;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }


    public Double getUpDownRate() {
        return upDownRate;
    }

    public void setUpDownRate(Double upDownRate) {
        this.upDownRate = upDownRate;
    }

    public void importWebElement(WebElement webElement){

        try {
            this.elementText = webElement.getText();

            // get table columns
            List<WebElement> webElementList = webElement.findElements(By.tagName("td"));

            this.stockCode = webElementList.get(1).getText();
            this.stockName = webElementList.get(2).getText();
            this.financialCount = Integer.valueOf(webElementList.get(4).getText());
            this.stockCount = Double.valueOf(webElementList.get(5).getText());
            this.stockRate = Double.valueOf(webElementList.get(6).getText());
            this.upDown = webElementList.get(7).getText();
            this.upDownCount = Double.valueOf(webElementList.get(8).getText());
            this.upDownRate = Double.valueOf(webElementList.get(9).getText());
        }
        catch ( Exception e )
        {
            System.out.println(e.getStackTrace()) ;
            throw e ;
        }
    }

}
