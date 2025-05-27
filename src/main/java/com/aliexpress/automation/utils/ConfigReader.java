package com.aliexpress.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class to read configuration properties from config.properties file
 */
public class ConfigReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigReader.class.getName());
    private Properties properties;
    private static final String CONFIG_FILE = "src/test/resources/config.properties";

    public ConfigReader() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(inputStream);
            LOGGER.info("Successfully loaded properties from: " + CONFIG_FILE);
        } catch (IOException e) {
            LOGGER.severe("Failed to load properties file: " + e.getMessage());
        }
    }

    /**
     * Gets property value from config file
     *
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets property value with default fallback
     *
     * @param key          Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}