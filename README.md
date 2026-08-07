<div align="center">

# 🍊 **OrangeHRM Advanced BDD Cucumber Test Automation Framework**

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.35.0-brightgreen?style=for-the-badge&logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.23.0-purple?style=for-the-badge&logo=cucumber&logoColor=white)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red?style=for-the-badge&logo=testng&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

*An enterprise-grade, behavior-driven development (BDD) test automation framework built for the OrangeHRM application, featuring robust Page Object Model (POM) architecture, data-driven testing, comprehensive logging with Log4j2, and parallel execution capabilities.*

</div>

---

## 🧭 **Table of Contents**
- [🌟 Overview](#-overview)
- [🏗️ Architecture & Design Patterns](#️-architecture--design-patterns)
- [🧰 Tech Stack & Dependencies](#-tech-stack--dependencies)
- [📁 Project Directory Structure](#-project-directory-structure)
- [📋 Feature Modules & Test Scenarios](#-feature-modules--test-scenarios)
- [⚙️ Prerequisites & Environment Setup](#️-prerequisites--environment-setup)
- [🚀 Execution Guide](#-execution-guide)
- [📊 Logging & Reporting](#-logging--reporting)
- [👤 Author & Acknowledgments](#-author--acknowledgments)

---

## 🌟 **Overview**

This repository houses an advanced, modular test automation suite designed for **OrangeHRM**, a widely utilized open-source Human Resource Management system. By leveraging **Behavior-Driven Development (BDD)** with **Cucumber** and **Gherkin syntax**, this framework bridges the gap between business stakeholders, QA engineers, and developers through executable specifications.

### **Key Highlights:**
- **BDD Approach**: Clear, human-readable Gherkin feature files (`.feature`) defining software behavior.
- **Robust Design Pattern**: Strict adherence to **Page Object Model (POM)** ensuring clean separation between test scripts and page locators/actions.
- **Data-Driven Testing**: Scenario Outlines supporting multiple credential sets and parameterized test inputs.
- **Enterprise Logging**: Fully configured **Log4j2** framework tracking detailed execution steps, warnings, and errors.
- **Cross-Browser & Dynamic Drivers**: Automated driver management via **WebDriverManager**.

---

## 🏗️ **Architecture & Design Patterns**

```
 ┌─────────────────────────────────────────────────────────┐
 │                   Gherkin Feature Files                 │
 │                  (Login.feature, etc.)                  │
 └────────────────────────────┬────────────────────────────┘
                              │
                              ▼
 ┌─────────────────────────────────────────────────────────┐
 │                 Step Definitions Layer                  │
 │                       (Steps1.java)                     │
 └────────────────────────────┬────────────────────────────┘
                              │
                              ▼
 ┌─────────────────────────────────────────────────────────┐
 │                  Page Objects Layer                     │
 │      (LoginPage, HomePage, CandidatePage, etc.)         │
 └────────────────────────────┬────────────────────────────┘
                              │
                              ▼
 ┌─────────────────────────────────────────────────────────┐
 │                     Base Class & Utilities              │
 │          (Driver Init, Config Reader, Wait Helper)       │
 └─────────────────────────────────────────────────────────┘
```

- **Page Object Model (POM)**: Encapsulates page elements and operations within dedicated classes under `pageObjects/`.
- **Base Class Management**: Centralized driver initialization, browser launching, URL loading, and teardown operations in `Baseclass.java`.
- **Helper Utilities**: Specialized helper classes for dynamic waits (`WaitHelper.java`), table handling (`TableHelper.java`), and configuration management (`config.properties`).

---

## 🧰 **Tech Stack & Dependencies**

| Technology | Version | Purpose |
| :--- | :--- | :--- |
| **Java** | `21` | Core Programming Language |
| **Selenium WebDriver** | `4.35.0` | Browser Automation Engine |
| **Cucumber JVM** | `7.23.0` | BDD Framework & Gherkin Parser |
| **TestNG** | `7.11.0` | Test Execution & Assertion Engine |
| **WebDriverManager** | `6.2.0` | Automated Binary Driver Management |
| **Apache POI** | `5.4.1` | Excel Data-Driven Support |
| **Log4j2** | `2.25.1` | Advanced Logging Framework |
| **ExtentReports** | `5.1.2` | Rich HTML Test Reporting |
| **Apache Maven** | `3.9+` | Build Automation & Dependency Management |

---

## 📁 **Project Directory Structure**

```text
2026_OrangeHRM_Demo_BDD_Cucumberproject/
│
├── Features/
│   ├── Candidate.feature         # Recruitment & Candidate test scenarios
│   └── Login.feature             # Authentication & Data-driven login scenarios
│
├── SameerNotes/                  # Framework documentation & implementation notes
│   ├── page1
│   ├── page2
│   ├── page3
│   └── page3ImplematationofLog4jconfig.txt
│
├── logs/
│   └── application.log           # Log4j2 execution logs
│
├── src/
│   ├── main/java/
│   │   └── Test1.java            # Main utility / scratchpad class
│   │
│   └── test/
│       ├── java/
│       │   ├── pageObjects/
│       │   │   ├── CandidatePage.java  # Candidate recruitment page elements & actions
│       │   │   ├── HomePage.java       # Dashboard & navigation elements
│       │   │   └── LoginPage.java      # Login form locators & methods
│       │   │
│       │   ├── stepDefiniations/
│       │   │   ├── Baseclass.java      # Common setup, teardown & driver state
│       │   │   └── Steps1.java         # Glue code mapping Gherkin steps to Java methods
│       │   │
│       │   ├── testRunner/
│       │   │   └── TestCaserun.java    # TestNG Cucumber Runner configuration
│       │   │
│       │   └── utilities/
│       │       ├── TableHelper.java    # Web table interaction utilities
│       │       └── WaitHelper.java     # Explicit & implicit wait handlers
│       │
│       └── resources/
│           ├── config.properties       # Environment configuration (URL, credentials)
│           └── log4j2.xml              # Log4j2 logging configuration
│
├── pom.xml                         # Maven project object model & dependencies
└── README.md                       # Project documentation
```

---

## 📋 **Feature Modules & Test Scenarios**

### 1. 🔐 **Authentication Feature (`Login.feature`)**
- **Valid Login**: Verifies successful authentication with valid administrator credentials, dashboard redirection, and secure logout.
- **Data-Driven Login (Scenario Outline)**: Validates system behavior across multiple credential combinations (valid vs. invalid inputs).

### 2. 👥 **Recruitment / Candidate Feature (`Candidate.feature`)**
- **Add Candidate**: Navigates to the Recruitment module, adds a new candidate with full details, saves record, and verifies success confirmation message (`Successfully Saved`).
- **Search Candidate**: Searches candidates by vacancy criteria (e.g., Senior QA Lead) and validates record population in web tables.

---

## ⚙️ **Prerequisites & Environment Setup**

Before running the test suite, ensure your development workstation meets the following requirements:
- **Java Development Kit (JDK) 21+** installed and configured in `JAVA_HOME`.
- **Apache Maven 3.9+** installed and added to system `PATH`.
- **IntelliJ IDEA** (Recommended IDE with Cucumber for Java and TestNG plugins).
- **Google Chrome** (or compatible modern browser).

---

## 🚀 **Execution Guide**

### **Method 1: Run via Maven Command Line**
Open terminal at the root directory and execute:
```bash
mvn clean test
```

### **Method 2: Run via TestNG Test Runner (`TestCaserun.java`)**
1. Open the project in IntelliJ IDEA.
2. Navigate to `src/test/java/testRunner/TestCaserun.java`.
3. Right-click on `TestCaserun.java` and select **Run 'TestCaserun'**.

---

## 📊 **Logging & Reporting**

- **Log4j2 Logging**: Every test execution generates detailed event logs stored in `/logs/application.log`, recording driver initialization, step execution, and exception traces.
- **Extent Reports**: Generates interactive HTML test reports containing test summaries, execution status, and timestamped metrics.

---

## 👤 **Author & Acknowledgments**

- **Author**: Sameer Programmer
- **Project**: 2026 OrangeHRM BDD Cucumber Automation Suite (`advance` branch)

<div align="center">
  <p>⭐ Star this repository if you find it helpful!</p>
</div>
