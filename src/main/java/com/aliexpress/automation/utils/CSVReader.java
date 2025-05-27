package com.aliexpress.automation.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility class for reading data from CSV files
 */
public class CSVReader {
    private static final Logger LOGGER = Logger.getLogger(CSVReader.class.getName());
    private String filePath;
    private String delimiter;

    public CSVReader(String filePath) {
        this(filePath, ",");
    }

    public CSVReader(String filePath, String delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    /**
     * Reads all data from a CSV file
     *
     * @return List of arrays containing the CSV data
     */
    public List<String[]> readAll() {
        List<String[]> allData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                allData.add(values);
            }
            LOGGER.info("Successfully read CSV data from: " + filePath);
        } catch (IOException e) {
            LOGGER.severe("Error reading CSV file: " + e.getMessage());
        }
        
        return allData;
    }

    /**
     * Reads CSV data as a list of maps
     * Each map represents a row with column names as keys
     *
     * @return List of maps with column names as keys and cell data as values
     */
    public List<Map<String, String>> readAllAsMap() {
        List<Map<String, String>> allData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            
            // Read header line
            if ((line = br.readLine()) != null) {
                headers = line.split(delimiter);
            }
            
            // Read data lines
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                Map<String, String> rowData = new HashMap<>();
                
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    rowData.put(headers[i], values[i]);
                }
                
                allData.add(rowData);
            }
            
            LOGGER.info("Successfully read CSV data as map from: " + filePath);
        } catch (IOException e) {
            LOGGER.severe("Error reading CSV file: " + e.getMessage());
        }
        
        return allData;
    }

    /**
     * Reads specific column data from CSV file
     *
     * @param columnIndex Index of the column to read (0-based)
     * @return Array of values from the specified column
     */
    public String[] readColumn(int columnIndex) {
        List<String> columnData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = line.split(delimiter);
                if (columnIndex < values.length) {
                    columnData.add(values[columnIndex]);
                } else {
                    columnData.add("");
                }
            }
            
            LOGGER.info("Successfully read column data from CSV");
        } catch (IOException e) {
            LOGGER.severe("Error reading CSV column data: " + e.getMessage());
        }
        
        return columnData.toArray(new String[0]);
    }

    /**
     * Reads specific column data by column name
     *
     * @param columnName Name of the column to read
     * @return Array of values from the specified column
     */
    public String[] readColumnByName(String columnName) {
        List<String> columnData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            // Read header row
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(delimiter);
                int columnIndex = -1;
                
                // Find the index of the requested column
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equals(columnName)) {
                        columnIndex = i;
                        break;
                    }
                }
                
                if (columnIndex != -1) {
                    // Read data for the specified column
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(delimiter);
                        if (columnIndex < values.length) {
                            columnData.add(values[columnIndex]);
                        } else {
                            columnData.add("");
                        }
                    }
                } else {
                    LOGGER.warning("Column '" + columnName + "' not found in CSV file");
                }
            }
            
            LOGGER.info("Successfully read column '" + columnName + "' data from CSV");
        } catch (IOException e) {
            LOGGER.severe("Error reading CSV column data: " + e.getMessage());
        }
        
        return columnData.toArray(new String[0]);
    }
}