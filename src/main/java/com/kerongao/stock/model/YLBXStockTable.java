package com.kerongao.stock.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class YLBXStockTable {

    /**
     * 0 序	1:股票代码	2:股票名称	3:原价	4:现价	5:区间涨幅	6:股东类型	7:更新日期	8:股东名称
     * 9:持股数(万)	10:市值(亿)	 11:类型	12:比例 (%)	 13:类型	14:增减仓	15:数量
     *
     */

    private String stockCod1 ;

    private String stockName2 ;

    private String oldPrice3 ;

    private String newPrice4 ;

    private String priceRate5 ;

    private String period7 ;

    private String StockCount9 ;

    private String StockValue10 ;

    private String stockRate12 ;

    private String upDown14 ;

    private String unDownCount15 ;

    public String getStockCod1() {
        return stockCod1;
    }

    public void setStockCod1(String stockCod1) {
        this.stockCod1 = stockCod1;
    }

    public String getStockName2() {
        return stockName2;
    }

    public void setStockName2(String stockName2) {
        this.stockName2 = stockName2;
    }

    public String getOldPrice3() {
        return oldPrice3;
    }

    public void setOldPrice3(String oldPrice3) {
        this.oldPrice3 = oldPrice3;
    }

    public String getNewPrice4() {
        return newPrice4;
    }

    public void setNewPrice4(String newPrice4) {
        this.newPrice4 = newPrice4;
    }

    public String getPriceRate5() {
        return priceRate5;
    }

    public void setPriceRate5(String priceRate5) {
        this.priceRate5 = priceRate5;
    }

    public String getPeriod7() {
        return period7;
    }

    public void setPeriod7(String period7) {
        this.period7 = period7;
    }

    public String getStockCount9() {
        return StockCount9;
    }

    public void setStockCount9(String stockCount9) {
        StockCount9 = stockCount9;
    }

    public String getStockValue10() {
        return StockValue10;
    }

    public void setStockValue10(String stockValue10) {
        StockValue10 = stockValue10;
    }

    public String getStockRate12() {
        return stockRate12;
    }

    public void setStockRate12(String stockRate12) {
        this.stockRate12 = stockRate12;
    }

    public String getUpDown14() {
        return upDown14;
    }

    public void setUpDown14(String upDown14) {
        this.upDown14 = upDown14;
    }

    public String getUnDownCount15() {
        return unDownCount15;
    }

    public void setUnDownCount15(String unDownCount15) {
        this.unDownCount15 = unDownCount15;
    }

    public void importRow(WebElement rowWebElement){

        List<WebElement> tdWebElements = rowWebElement.findElements(By.tagName("td")) ;

        this.stockCod1 = tdWebElements.get(1).getText() ;

        this.stockName2 = tdWebElements.get(2).getText() ;

        this.oldPrice3 = tdWebElements.get(3).getText()  ;

        this.newPrice4 = tdWebElements.get(4).getText() ;

        this.priceRate5 = tdWebElements.get(5).getText() ;

        this.period7 = tdWebElements.get(7).getText() ;

        this.StockCount9 = tdWebElements.get(9).getText()  ;

        this.StockValue10 =  tdWebElements.get(10).getText() ;

        this.stockRate12 = tdWebElements.get(12).getText() ;

        this.upDown14 = tdWebElements.get(14).getText() ;

        this.unDownCount15 =  tdWebElements.get(15).getText() ;

    }

}
