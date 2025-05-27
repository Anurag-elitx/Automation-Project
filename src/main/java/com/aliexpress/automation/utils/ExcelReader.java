package com.aliexpress.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility class for reading data from Excel files using Apache POI
 */
public class ExcelReader {
    private static final Logger LOGGER = Logger.getLogger(ExcelReader.class.getName());
    private String filePath;
    private FileInputStream fileInputStream;
    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private Cell cell;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets data from a specific sheet in the Excel file as a list of maps
     * Each map represents a row with column names as keys
     *
     * @param sheetName Name of the sheet
     * @return List of maps containing the data
     */
    public List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> excelData = new ArrayList<>();
        try {
            fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
            
            // Get header row for column names
            Row headerRow = sheet.getRow(0);
            
            // Iterate through data rows
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell dataCell = currentRow.getCell(j);
                    rowData.put(getCellValueAsString(headerCell), getCellValueAsString(dataCell));
                }
                
                excelData.add(rowData);
            }
            
            fileInputStream.close();
            workbook.close();
            
            LOGGER.info("Successfully read data from sheet: " + sheetName);
        } catch (IOException e) {
            LOGGER.severe("Error reading Excel file: " + e.getMessage());
        }
        
        return excelData;
    }

    /**
     * Gets data from specific cell by row and column index
     *
     * @param sheetName  Name of the sheet
     * @param rowNum     Row number (0-based)
     * @param columnNum  Column number (0-based)
     * @return Cell value as string
     */
    public String getCellData(String sheetName, int rowNum, int columnNum) {
        try {
            fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
            row = sheet.getRow(rowNum);
            cell = row.getCell(columnNum);
            
            String cellValue = getCellValueAsString(cell);
            
            fileInputStream.close();
            workbook.close();
            return cellValue;
        } catch (IOException e) {
            LOGGER.severe("Error reading cell data: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets specific column data as an array
     *
     * @param sheetName  Name of the sheet
     * @param columnName Column name
     * @return Array of column values
     */
    public String[] getColumnData(String sheetName, String columnName) {
        try {
            fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
            
            // Find column index from header row
            Row headerRow = sheet.getRow(0);
            int columnIndex = -1;
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                if (headerRow.getCell(i).getStringCellValue().equals(columnName)) {
                    columnIndex = i;
                    break;
                }
            }
            
            if (columnIndex == -1) {
                LOGGER.warning("Column '" + columnName + "' not found in sheet: " + sheetName);
                return new String[0];
            }
            
            // Read column data
            String[] columnData = new String[sheet.getPhysicalNumberOfRows() - 1];
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row != null && row.getCell(columnIndex) != null) {
                    columnData[i - 1] = getCellValueAsString(row.getCell(columnIndex));
                }
            }
            
            fileInputStream.close();
            workbook.close();
            return columnData;
        } catch (IOException e) {
            LOGGER.severe("Error reading column data: " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * Converts cell value to string regardless of the cell type
     *
     * @param cell Cell to get value from
     * @return String representation of cell value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // To avoid scientific notation for numbers
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}