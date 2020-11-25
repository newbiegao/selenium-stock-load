package com.kerongao.stock;

import com.kerongao.stock.model.StockTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CVSFileDataService {

    private static Logger logger = LoggerFactory.getLogger(CVSFileDataService.class) ;

    @Autowired
    private LoadStockDataAction loadStockDataAction ;

    public void loadStockDataToExcelFile( Integer periodLimit  ){

        List<StockTable> stockTableList = loadStockDataAction.loadStockAllPeriodData( periodLimit ) ;

        writeToExcelFile(stockTableList) ;

    }

    public void writeToExcelFile(List<StockTable> stockTableList ){

        Workbook workbook = new XSSFWorkbook();
        String currentSheetName = "" ;
        Sheet sheet = null ;
        Integer rowIndex = 1 ;
        for( StockTable stockTable : stockTableList ){

            if(!stockTable.getPeriod().equalsIgnoreCase(currentSheetName)) {
                currentSheetName = stockTable.getPeriod() ;
                sheet = workbook.createSheet(currentSheetName);
                createHeader(workbook , sheet) ;
                rowIndex = 1 ;
            }
            // add row to sheet
            addRow(workbook , sheet , stockTable , rowIndex ) ;

            rowIndex++ ;
        }

        saveFile(workbook) ;
    }

    private void createHeader( Workbook workbook ,  Sheet sheet ){

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

    private void addRow( Workbook workbook ,  Sheet sheet , StockTable stockTable , Integer rowIndex ){

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

    private void saveFile( Workbook workbook ){

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);

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
