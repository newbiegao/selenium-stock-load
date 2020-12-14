package com.kerongao.stock;

import com.kerongao.stock.model.FundData;
import com.kerongao.stock.model.StockTable;
import com.kerongao.stock.model.YLBXStockTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CVSFileDataService {

    private static Logger logger = LoggerFactory.getLogger(CVSFileDataService.class) ;

    @Autowired
    private LoadStockDataAction loadStockDataAction ;

    @Autowired
    private YLBXStockPageService ylbxStockPageService ;

    @Autowired
    private LoadFundDataListAction loadFundDataListAction ;

    /**
     * load period stock data by periodLimit to excel file
     * @param periodLimit if periodLimit < 0 then load all period stock data
     */
    public void saveStockDataToExcelFile( Integer periodLimit  ){

        List<StockTable> stockTableList = loadStockDataAction.loadStockAllPeriodData( periodLimit ) ;
        writeStockDataToExcelFile(stockTableList) ;

    }

    /**
     * load period stock data by period range  to excel file
     * @param start  period start
     * @param periodLength period count
     */
    public void saveStockDataToExcelFile(Integer start  ,  Integer periodLength  ){

        List<StockTable> stockTableList = loadStockDataAction.loadStockAllPeriodDataByRange(start , periodLength) ;
        writeStockDataToExcelFile(stockTableList) ;

    }

    /**
     * load and save YLBX stock date to excel file
     */
    public void saveYLBXStockDataToExcelFile(){

        List<YLBXStockTable> ylbxStockTableList = ylbxStockPageService.loadYLBXStockDataRowsToDataModle() ;
        writeYLBXStockTableToExcelFile(ylbxStockTableList) ;

    }

    /**
     * load and save Fund name and url list to excel file
     */
    public void saveFundListToExcelFile(){

        List<FundData> fundDataList = loadFundDataListAction.loadCurrentPeriodStockFundList() ;
        writeFundDataListToExcelFile(fundDataList) ;

    }

    private void writeFundDataListToExcelFile( List<FundData>  fundDataList ) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("社保基金名称列表");

        Integer index = 0  ;
        for( FundData fundData : fundDataList ){
            addFundDataToRow( workbook , sheet ,  fundData , index ) ;
            index++ ;
        }

        String path = saveFile(workbook) ;
        logger.debug(" load Fund data list info Excel file , path : {} " , path );

    }

    private void addFundDataToRow(Workbook workbook ,  Sheet sheet ,  FundData fundData , Integer index ){

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(index);

        // 社保名称
        Cell cell = row.createCell(0);
        cell.setCellValue(fundData.getName());
        cell.setCellStyle(style);

        // 社保持仓连接地址
        cell = row.createCell(1);
        cell.setCellValue(fundData.getUrl());
        cell.setCellStyle(style);

    }

    private void writeYLBXStockTableToExcelFile( List<YLBXStockTable> ylbxStockTables ){

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("养老保险基金股票池");

        Integer index = 0  ;
        for( YLBXStockTable  ylbxStockTable :  ylbxStockTables ) {
            addYLBXStockDataTableRow( workbook , sheet , ylbxStockTable , index ) ;
            index++ ;

        } ;

        String path = saveFile(workbook) ;

        logger.debug(" load YLBX stock table info Excel file , path : {} " , path );

    }

    private void addYLBXStockDataTableRow( Workbook workbook ,  Sheet sheet , YLBXStockTable stockTable , Integer rowIndex ){

        /**
         *  1:股票代码	2:股票名称	3:原价	4:现价	5:区间涨幅 7:更新日期
         *  9:持股数(万)	10:市值(亿)	12:比例 (%)	14:增减仓	15:数量
         *
         */

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(rowIndex);

        // 股票代码
        Cell cell = row.createCell(0);
        cell.setCellValue(stockTable.getStockCod1());
        cell.setCellStyle(style);

        // 股票名称
        cell = row.createCell(1);
        cell.setCellValue(stockTable.getStockName2());
        cell.setCellStyle(style);

        // 原价
        cell = row.createCell(2);
        cell.setCellValue(stockTable.getOldPrice3());
        cell.setCellStyle(style);

        // 现价
        cell = row.createCell(3);
        cell.setCellValue(stockTable.getNewPrice4());
        cell.setCellStyle(style);

        // 区间涨幅
        cell = row.createCell(4);
        cell.setCellValue(stockTable.getPriceRate5());
        cell.setCellStyle(style);

        // 更新日期
        cell = row.createCell(5);
        cell.setCellValue(stockTable.getPeriod7());
        cell.setCellStyle(style);

        // 持股数(万)
        cell = row.createCell(6);
        cell.setCellValue(stockTable.getStockCount9());
        cell.setCellStyle(style);

        // 市值(亿)
        cell = row.createCell(7);
        cell.setCellValue(stockTable.getStockValue10());
        cell.setCellStyle(style);

        // 比例 (%)
        cell = row.createCell(8);
        cell.setCellValue(stockTable.getStockRate12());
        cell.setCellStyle(style);

        // 增减仓
        cell = row.createCell(9);
        cell.setCellValue(stockTable.getUpDown14());
        cell.setCellStyle(style);

        // 数量
        cell = row.createCell(10);
        cell.setCellValue(stockTable.getUnDownCount15());
        cell.setCellStyle(style);

    }

    private void createYLBXStockDataTableHeader( Workbook workbook ,  Sheet sheet ){

        /**
         *  1:股票代码	2:股票名称	3:原价	4:现价	5:区间涨幅 7:更新日期
         *  9:持股数(万)	10:市值(亿)	12:比例 (%)	14:增减仓	15:数量
         *
         */

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);

        Row header = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // column0
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("股票代码");
        headerCell.setCellStyle(headerStyle);

        // column1
        headerCell = header.createCell(1);
        headerCell.setCellValue("股票名称");
        headerCell.setCellStyle(headerStyle);

        // column2
        headerCell = header.createCell(2);
        headerCell.setCellValue("原价");
        headerCell.setCellStyle(headerStyle);

        // column3
        headerCell = header.createCell(3);
        headerCell.setCellValue("现价");
        headerCell.setCellStyle(headerStyle);

        // column4
        headerCell = header.createCell(4);
        headerCell.setCellValue("区间涨幅");
        headerCell.setCellStyle(headerStyle);

        // column5
        headerCell = header.createCell(5);
        headerCell.setCellValue("更新日期");
        headerCell.setCellStyle(headerStyle);

        // column6
        headerCell = header.createCell(6);
        headerCell.setCellValue("持股数(万)");
        headerCell.setCellStyle(headerStyle);

        // column7
        headerCell = header.createCell(7);
        headerCell.setCellValue("市值(亿)");
        headerCell.setCellStyle(headerStyle);

        // column8
        headerCell = header.createCell(8);
        headerCell.setCellValue("比例 (%)");
        headerCell.setCellStyle(headerStyle);

        // column9
        headerCell = header.createCell(9);
        headerCell.setCellValue("增减仓");
        headerCell.setCellStyle(headerStyle);

        // column10
        headerCell = header.createCell(10);
        headerCell.setCellValue("数量");
        headerCell.setCellStyle(headerStyle);

    }


    private void writeStockDataToExcelFile(List<StockTable> stockTableList ){

        Workbook workbook = new XSSFWorkbook();
        String currentSheetName = "" ;
        Sheet sheet = null ;
        Integer rowIndex = 1 ;
        Integer sheetCount = 0 ;
        Integer rowCount = 0  ;
        for( StockTable stockTable : stockTableList ){

            rowCount++ ;

            if(!stockTable.getPeriod().equalsIgnoreCase(currentSheetName)) {
                currentSheetName = stockTable.getPeriod() ;
                sheet = workbook.createSheet(currentSheetName);
                createStockDataTableHeader(workbook , sheet) ;
                rowIndex = 1 ;
                sheetCount ++ ;
            }
            // add row to sheet
            addStockDataTableRow(workbook , sheet , stockTable , rowIndex ) ;

            rowIndex++ ;
        }

        String fileName =  saveFile(workbook) ;
        logger.debug(" load stock data rowCount : {}  , sheetCount: {} ,  and save to file : {} " , rowCount , sheetCount , fileName );
    }

    private void createStockDataTableHeader( Workbook workbook ,  Sheet sheet ){

        // 股票代码	股票简称		持有社保家数(家)	持股总数(万股)	占总股本比例(%)	持股变化	 持股变动数值(万股)  持股变动比例(%)

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);

        Row header = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // column0
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("股票代码");
        headerCell.setCellStyle(headerStyle);

        // column1
        headerCell = header.createCell(1);
        headerCell.setCellValue("股票简称");
        headerCell.setCellStyle(headerStyle);

        // column2
        headerCell = header.createCell(2);
        headerCell.setCellValue("持有社保家数(家)");
        headerCell.setCellStyle(headerStyle);

        // column3
        headerCell = header.createCell(3);
        headerCell.setCellValue("持股总数(万股)");
        headerCell.setCellStyle(headerStyle);

        // column4
        headerCell = header.createCell(4);
        headerCell.setCellValue("占总股本比例(%)");
        headerCell.setCellStyle(headerStyle);

        // column5
        headerCell = header.createCell(5);
        headerCell.setCellValue("持股变化");
        headerCell.setCellStyle(headerStyle);

        // column6
        headerCell = header.createCell(6);
        headerCell.setCellValue("持股变动数值(万股)");
        headerCell.setCellStyle(headerStyle);

        // column7
        headerCell = header.createCell(7);
        headerCell.setCellValue("持股变动比例(%)");
        headerCell.setCellStyle(headerStyle);

    }

    private void addStockDataTableRow( Workbook workbook ,  Sheet sheet , StockTable stockTable , Integer rowIndex ){

        // 股票代码	股票简称		持有社保家数(家)	持股总数(万股)	占总股本比例(%)	持股变化	 持股变动数值(万股)  持股变动比例(%)

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(rowIndex);

        // 股票代码
        Cell cell = row.createCell(0);
        cell.setCellValue(stockTable.getStockCode());
        cell.setCellStyle(style);

        // 股票简称
        cell = row.createCell(1);
        cell.setCellValue(stockTable.getStockName());
        cell.setCellStyle(style);

        // 持有社保家数(家)
        cell = row.createCell(2);
        cell.setCellValue(stockTable.getFinancialCount());
        cell.setCellStyle(style);

        // 持股总数(万股)
        cell = row.createCell(3);
        cell.setCellValue(stockTable.getStockCount());
        cell.setCellStyle(style);

        // 占总股本比例(%)
        cell = row.createCell(4);
        cell.setCellValue(stockTable.getStockRate());
        cell.setCellStyle(style);

        // 持股变化
        cell = row.createCell(5);
        cell.setCellValue(stockTable.getUpDown());
        cell.setCellStyle(style);

        // 持股变动数值(万股)
        cell = row.createCell(6);
        cell.setCellValue(stockTable.getUpDownCount());
        cell.setCellStyle(style);

        // 持股变动比例(%)
        cell = row.createCell(7);
        cell.setCellValue(stockTable.getUpDownRate());
        cell.setCellStyle(style);

    }

    private String  saveFile( Workbook workbook ){

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss") ;

        String fileName = df.format( new Date() ) + "-" + "stock.xlsx" ;
        String fileLocation = path.substring(0, path.length() - 1) + fileName ;

        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            return fileLocation ;

        }
        catch ( Exception e )
        {
            throw new  RuntimeException( e) ;
        }
        finally {
            try { workbook.close() ; } catch (IOException e ){ }
        }

    }

}
