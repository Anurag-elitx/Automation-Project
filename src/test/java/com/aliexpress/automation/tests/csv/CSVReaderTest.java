package com.aliexpress.automation.tests.csv;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.utils.CSVReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test class demonstrating CSV file reading in test automation
 */
public class CSVReaderTest extends BaseTest {
    
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    private static final String CSV_FILE = TEST_DATA_PATH + "searchData.csv";
    
    /**
     * Set up test data before tests
     */
    @BeforeClass
    public void setupTestData() {
        LOGGER.info("Setting up test data for CSV tests");
        
        try {
            // Create directory if it doesn't exist
            File directory = new File(TEST_DATA_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
                LOGGER.info("Created test data directory: " + TEST_DATA_PATH);
            }
            
            // Create a sample CSV file for testing
            createSampleCSVFile();
            
            LOGGER.info("Successfully created test data");
        } catch (Exception e) {
            LOGGER.severe("Error creating test data: " + e.getMessage());
        }
    }
    
    /**
     * Test reading all data from CSV file
     */
    @Test
    public void testReadAllCSVData() {
        LOGGER.info("Starting test: testReadAllCSVData");
        
        try {
            // Create CSV reader
            CSVReader csvReader = new CSVReader(CSV_FILE);
            
            // Read all data
            List<String[]> allData = csvReader.readAll();
            
            // Verify data was read
            Assert.assertNotNull(allData, "Should read data from CSV file");
            Assert.assertFalse(allData.isEmpty(), "Data list should not be empty");
            
            LOGGER.info("Read " + allData.size() + " rows from CSV file");
            
            // Log data for verification (skip header row)
            for (int i = 1; i < allData.size(); i++) {
                String[] row = allData.get(i);
                LOGGER.info("Row " + i + ": Search Term: " + row[0] + 
                           ", Category: " + row[1] +
                           ", Min Results: " + row[2]);
            }
            
            Assert.assertTrue(true, "Successfully read all CSV data");
        } catch (Exception e) {
            LOGGER.severe("Error reading CSV data: " + e.getMessage());
            Assert.fail("Failed to read CSV data: " + e.getMessage());
        }
    }
    
    /**
     * Test reading CSV data as a list of maps
     */
    @Test
    public void testReadCSVDataAsMaps() {
        LOGGER.info("Starting test: testReadCSVDataAsMaps");
        
        try {
            // Create CSV reader
            CSVReader csvReader = new CSVReader(CSV_FILE);
            
            // Read data as maps
            List<Map<String, String>> dataAsMaps = csvReader.readAllAsMap();
            
            // Verify data was read
            Assert.assertNotNull(dataAsMaps, "Should read data from CSV file as maps");
            Assert.assertFalse(dataAsMaps.isEmpty(), "Data map list should not be empty");
            
            LOGGER.info("Read " + dataAsMaps.size() + " rows from CSV file as maps");
            
            // Log data for verification
            for (Map<String, String> row : dataAsMaps) {
                LOGGER.info("Search Term: " + row.get("SearchTerm") + 
                           ", Category: " + row.get("Category") +
                           ", Min Results: " + row.get("MinResults"));
            }
            
            Assert.assertTrue(true, "Successfully read CSV data as maps");
        } catch (Exception e) {
            LOGGER.severe("Error reading CSV data as maps: " + e.getMessage());
            Assert.fail("Failed to read CSV data as maps: " + e.getMessage());
        }
    }
    
    /**
     * Test reading specific column data from CSV
     */
    @Test
    public void testReadColumnData() {
        LOGGER.info("Starting test: testReadColumnData");
        
        try {
            // Create CSV reader
            CSVReader csvReader = new CSVReader(CSV_FILE);
            
            // Read "SearchTerm" column by index (0)
            String[] searchTerms = csvReader.readColumn(0);
            
            // Verify data was read
            Assert.assertNotNull(searchTerms, "Column data should not be null");
            Assert.assertTrue(searchTerms.length > 0, "Column data should not be empty");
            
            LOGGER.info("Read " + searchTerms.length + " search terms from column 0");
            
            // Log data for verification
            for (String searchTerm : searchTerms) {
                LOGGER.info("Search Term: " + searchTerm);
            }
            
            // Read "Category" column by name
            String[] categories = csvReader.readColumnByName("Category");
            
            // Verify data was read
            Assert.assertNotNull(categories, "Column data should not be null");
            Assert.assertTrue(categories.length > 0, "Column data should not be empty");
            
            LOGGER.info("Read " + categories.length + " categories from Category column");
            
            // Log data for verification
            for (String category : categories) {
                LOGGER.info("Category: " + category);
            }
            
            Assert.assertTrue(true, "Successfully read column data from CSV");
        } catch (Exception e) {
            LOGGER.severe("Error reading column data: " + e.getMessage());
            Assert.fail("Failed to read column data: " + e.getMessage());
        }
    }
    
    /**
     * Test data-driven search using CSV data
     */
    @Test
    public void testDataDrivenSearchWithCSV() {
        LOGGER.info("Starting test: testDataDrivenSearchWithCSV");
        
        try {
            // Create CSV reader
            CSVReader csvReader = new CSVReader(CSV_FILE);
            
            // Read search terms column
            String[] searchTerms = csvReader.readColumn(0);
            
            // Limit to first 2 terms to save time
            int termsToTest = Math.min(2, searchTerms.length);
            
            for (int i = 0; i < termsToTest; i++) {
                String searchTerm = searchTerms[i];
                LOGGER.info("Testing search with term: " + searchTerm);
                
                HomePage homePage = new HomePage(driver);
                
                // Close popup if it appears
                homePage.closePopupIfPresent();
                
                // Search for product
                homePage.searchProductByCss(searchTerm);
                
                // Capture screenshot of results
                String screenshotPath = ScreenshotUtils.captureScreenshot(driver, "CSV_Search_" + searchTerm);
                LOGGER.info("Captured search results screenshot: " + screenshotPath);
                
                // Navigate back to home page for next search
                driver.get(configReader.getProperty("base.url", "https://aliexpress.com"));
            }
            
            LOGGER.info("Successfully completed data-driven search with CSV data");
            Assert.assertTrue(true, "Successfully completed data-driven search");
        } catch (Exception e) {
            LOGGER.severe("Error in data-driven search: " + e.getMessage());
            Assert.fail("Failed in data-driven search: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to create a sample CSV file for testing
     */
    private void createSampleCSVFile() {
        try {
            LOGGER.info("Creating sample CSV file at: " + CSV_FILE);
            
            try (FileWriter writer = new FileWriter(CSV_FILE)) {
                // Write header
                writer.append("SearchTerm,Category,MinResults\n");
                
                // Write data rows
                writer.append("smartphone,Electronics,10\n");
                writer.append("laptop,Computers,10\n");
                writer.append("headphones,Audio,10\n");
                writer.append("smart watch,Wearables,10\n");
                writer.append("bluetooth speaker,Audio,10\n");
            }
            
            LOGGER.info("Successfully created sample CSV file");
        } catch (IOException e) {
            LOGGER.severe("Error creating sample CSV file: " + e.getMessage());
        }
    }
}