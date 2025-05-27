package com.aliexpress.automation.tests.excel;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Test class demonstrating Excel file reading in test automation
 */
public class ExcelReaderTest extends BaseTest {
    
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    private static final String EXCEL_FILE = TEST_DATA_PATH + "searchData.xlsx";
    
    /**
     * Set up test data before tests
     */
    @BeforeClass
    public void setupTestData() {
        LOGGER.info("Setting up test data for Excel tests");
        
        try {
            // Create directory if it doesn't exist
            File directory = new File(TEST_DATA_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
                LOGGER.info("Created test data directory: " + TEST_DATA_PATH);
            }
            
            // Create a sample Excel file for testing
            createSampleExcelFile();
            
            LOGGER.info("Successfully created test data");
        } catch (Exception e) {
            LOGGER.severe("Error creating test data: " + e.getMessage());
        }
    }
    
    /**
     * Test reading data from Excel as a list of maps
     */
    @Test
    public void testReadExcelDataAsMaps() {
        LOGGER.info("Starting test: testReadExcelDataAsMaps");
        
        try {
            // Create Excel reader
            ExcelReader excelReader = new ExcelReader(EXCEL_FILE);
            
            // Read data from "SearchTerms" sheet
            List<Map<String, String>> searchData = excelReader.getSheetData("SearchTerms");
            
            // Verify data was read
            Assert.assertNotNull(searchData, "Should read data from Excel file");
            Assert.assertFalse(searchData.isEmpty(), "Data list should not be empty");
            
            LOGGER.info("Read " + searchData.size() + " rows from Excel file");
            
            // Log data for verification
            for (Map<String, String> row : searchData) {
                LOGGER.info("Search Term: " + row.get("SearchTerm") + 
                           ", Category: " + row.get("Category") +
                           ", Min Results: " + row.get("MinResults"));
            }
            
            Assert.assertTrue(true, "Successfully read Excel data as maps");
        } catch (Exception e) {
            LOGGER.severe("Error reading Excel data: " + e.getMessage());
            Assert.fail("Failed to read Excel data: " + e.getMessage());
        }
    }
    
    /**
     * Test reading specific cell data from Excel
     */
    @Test
    public void testReadSpecificCellData() {
        LOGGER.info("Starting test: testReadSpecificCellData");
        
        try {
            // Create Excel reader
            ExcelReader excelReader = new ExcelReader(EXCEL_FILE);
            
            // Read specific cell data (row 1, column 0 - first data row, first column)
            String cellData = excelReader.getCellData("SearchTerms", 1, 0);
            
            LOGGER.info("Cell data at row 1, column 0: " + cellData);
            Assert.assertNotNull(cellData, "Cell data should not be null");
            Assert.assertFalse(cellData.isEmpty(), "Cell data should not be empty");
            
            // Read header cell
            String headerCell = excelReader.getCellData("SearchTerms", 0, 0);
            LOGGER.info("Header cell at row 0, column 0: " + headerCell);
            Assert.assertEquals(headerCell, "SearchTerm", "Header cell should match expected value");
            
            LOGGER.info("Successfully read specific cell data from Excel");
        } catch (Exception e) {
            LOGGER.severe("Error reading specific cell data: " + e.getMessage());
            Assert.fail("Failed to read specific cell data: " + e.getMessage());
        }
    }
    
    /**
     * Test reading column data from Excel
     */
    @Test
    public void testReadColumnData() {
        LOGGER.info("Starting test: testReadColumnData");
        
        try {
            // Create Excel reader
            ExcelReader excelReader = new ExcelReader(EXCEL_FILE);
            
            // Read data from "SearchTerm" column
            String[] searchTerms = excelReader.getColumnData("SearchTerms", "SearchTerm");
            
            // Verify data was read
            Assert.assertNotNull(searchTerms, "Column data should not be null");
            Assert.assertTrue(searchTerms.length > 0, "Column data should not be empty");
            
            LOGGER.info("Read " + searchTerms.length + " search terms from SearchTerm column");
            
            // Log data for verification
            for (String searchTerm : searchTerms) {
                LOGGER.info("Search Term: " + searchTerm);
            }
            
            // Read data from "Category" column
            String[] categories = excelReader.getColumnData("SearchTerms", "Category");
            
            // Verify data was read
            Assert.assertNotNull(categories, "Column data should not be null");
            Assert.assertTrue(categories.length > 0, "Column data should not be empty");
            
            LOGGER.info("Read " + categories.length + " categories from Category column");
            
            // Log data for verification
            for (String category : categories) {
                LOGGER.info("Category: " + category);
            }
            
            LOGGER.info("Successfully read column data from Excel");
        } catch (Exception e) {
            LOGGER.severe("Error reading column data: " + e.getMessage());
            Assert.fail("Failed to read column data: " + e.getMessage());
        }
    }
    
    /**
     * Test data-driven search using Excel data
     */
    @Test
    public void testDataDrivenSearchWithExcel() {
        LOGGER.info("Starting test: testDataDrivenSearchWithExcel");
        
        try {
            // Create Excel reader
            ExcelReader excelReader = new ExcelReader(EXCEL_FILE);
            
            // Read search data
            List<Map<String, String>> searchData = excelReader.getSheetData("SearchTerms");
            
            // Limit to first 2 rows to save time
            int rowsToTest = Math.min(2, searchData.size());
            
            for (int i = 0; i < rowsToTest; i++) {
                Map<String, String> testData = searchData.get(i);
                String searchTerm = testData.get("SearchTerm");
                
                LOGGER.info("Testing search with term: " + searchTerm);
                
                HomePage homePage = new HomePage(driver);
                
                // Close popup if it appears
                homePage.closePopupIfPresent();
                
                // Search for product
                homePage.searchProductByCss(searchTerm);
                
                // Capture screenshot of results
                String screenshotPath = ScreenshotUtils.captureScreenshot(driver, "Search_" + searchTerm);
                LOGGER.info("Captured search results screenshot: " + screenshotPath);
                
                // Navigate back to home page for next search
                driver.get(configReader.getProperty("base.url", "https://aliexpress.com"));
            }
            
            LOGGER.info("Successfully completed data-driven search with Excel data");
            Assert.assertTrue(true, "Successfully completed data-driven search");
        } catch (Exception e) {
            LOGGER.severe("Error in data-driven search: " + e.getMessage());
            Assert.fail("Failed in data-driven search: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to create a sample Excel file for testing
     */
    private void createSampleExcelFile() {
        try {
            LOGGER.info("Creating sample Excel file at: " + EXCEL_FILE);
            
            // Create workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            
            // Create sheet
            XSSFSheet sheet = workbook.createSheet("SearchTerms");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("SearchTerm");
            
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Category");
            
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("MinResults");
            
            // Add data rows
            Row dataRow1 = sheet.createRow(1);
            dataRow1.createCell(0).setCellValue("smartphone");
            dataRow1.createCell(1).setCellValue("Electronics");
            dataRow1.createCell(2).setCellValue("10");
            
            Row dataRow2 = sheet.createRow(2);
            dataRow2.createCell(0).setCellValue("laptop");
            dataRow2.createCell(1).setCellValue("Computers");
            dataRow2.createCell(2).setCellValue("10");
            
            Row dataRow3 = sheet.createRow(3);
            dataRow3.createCell(0).setCellValue("headphones");
            dataRow3.createCell(1).setCellValue("Audio");
            dataRow3.createCell(2).setCellValue("10");
            
            Row dataRow4 = sheet.createRow(4);
            dataRow4.createCell(0).setCellValue("smart watch");
            dataRow4.createCell(1).setCellValue("Wearables");
            dataRow4.createCell(2).setCellValue("10");
            
            Row dataRow5 = sheet.createRow(5);
            dataRow5.createCell(0).setCellValue("bluetooth speaker");
            dataRow5.createCell(1).setCellValue("Audio");
            dataRow5.createCell(2).setCellValue("10");
            
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE)) {
                workbook.write(outputStream);
            }
            
            // Close workbook
            workbook.close();
            
            LOGGER.info("Successfully created sample Excel file");
        } catch (Exception e) {
            LOGGER.severe("Error creating sample Excel file: " + e.getMessage());
        }
    }
}