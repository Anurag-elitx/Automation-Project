# AliExpress Automation Testing Framework

A comprehensive Selenium Java test automation framework for AliExpress.com. This project demonstrates various Selenium testing techniques organized by topic.

## Project Structure

```
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── aliexpress/
│   │               └── automation/
│   │                   ├── base/       # Base test setup classes
│   │                   ├── pages/      # Page Objects (without Page Factory)
│   │                   ├── pages/factory/ # Page Objects using Page Factory
│   │                   ├── listeners/  # TestNG listeners
│   │                   └── utils/      # Utility classes
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── aliexpress/
│       │           └── automation/
│       │               └── tests/      # Test classes by topic
│       └── resources/
│           ├── testng.xml             # TestNG configuration
│           ├── config.properties      # Configuration properties
│           └── testdata/              # Test data files (Excel, CSV)
└── pom.xml                            # Maven project configuration
```

## Test Topics Covered

1. **XPath Locators** - Demonstrates finding elements using XPath
2. **CSS Selectors** - Demonstrates finding elements using CSS selectors
3. **Synchronization** - Demonstrates explicit, implicit, and fluent waits
4. **Exception Handling** - Demonstrates handling Selenium exceptions
5. **Select Class** - Demonstrates working with dropdown elements
6. **Action Class** - Demonstrates advanced user interactions (hover, drag, etc.)
7. **JavaScript Executor** - Demonstrates executing JavaScript
8. **Handling Alerts** - Demonstrates working with JavaScript alerts
9. **Handling iFrames** - Demonstrates switching between iFrames
10. **Handling Windows** - Demonstrates working with multiple browser windows/tabs
11. **TestNG Features** - Demonstrates TestNG functionality (groups, dependencies, etc.)
12. **Screenshots** - Demonstrates capturing screenshots during test execution
13. **Reading Excel Files** - Demonstrates reading test data from Excel
14. **Reading CSV Files** - Demonstrates reading test data from CSV
15. **ITestListener** - Demonstrates TestNG listeners for reporting
16. **Utilities** - Demonstrates reusable utility methods
17. **Page Object Model** - Demonstrates POM design pattern
18. **Page Factory** - Demonstrates POM using Page Factory

## Prerequisites

- Java 11 or higher
- Maven
- Chrome/Firefox/Edge browser

## Setup Instructions

1. Clone the repository
2. Import the project as a Maven project in your IDE
3. Run `mvn clean install` to download dependencies

## Running Tests

### Run all tests
```
mvn clean test
```

### Run specific test class
```
mvn clean test -Dtest=XPathTest
```

### Run specific test group
```
mvn clean test -Dgroups=smoke
```

### Run with specific browser
```
mvn clean test -Dbrowser=firefox
```

## Notes

- This framework uses dummy/simulated XPaths and selectors for AliExpress.com
- The tests are designed for demonstration and learning purposes
- Some tests may be skipped or modified to avoid actual interactions with the production site
- Screenshots, logs, and test results are saved in the `test-output` directory

## Utilities

The framework includes several utility classes:
- `WaitUtils` - For handling synchronization
- `JavaScriptUtils` - For JavaScript operations
- `ScreenshotUtils` - For capturing screenshots
- `ExcelReader` - For reading Excel data
- `CSVReader` - For reading CSV data
- `ConfigReader` - For reading configuration properties

## Page Objects

The framework demonstrates two approaches to the Page Object Model:
- Traditional POM without Page Factory (in `pages` package)
- POM with Page Factory (in `pages.factory` package)