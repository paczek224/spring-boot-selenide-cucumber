# spring-boot-selenide

A UI test automation framework that combines **Spring Boot** with **Selenide** and **Cucumber**, demonstrating how to write clean, maintainable browser tests using BDD scenarios, the Page Object Model pattern, custom assertions, and integrated Allure reporting.

---

## What this project does

- Boots a Spring context before tests to provide dependency injection and configuration management
- Uses Selenide for concise, readable browser interactions
- Applies the **Page Object Model** — each page is a Spring-managed bean with typed element accessors
- Expresses test cases as **Gherkin scenarios** (Given/When/Then) via Cucumber
- Generates rich **Allure reports** with automatic screenshots on failure, structured by Feature and Scenario
- Runs scenarios **in parallel** (2 threads) via Cucumber's built-in JUnit Platform executor
- Logs scenario pass/fail results via Cucumber `@After` hooks

---

## Tech stack

| Layer                | Technology                              | Version                  |
|----------------------|-----------------------------------------|--------------------------|
| Language             | Java                                    | 25                       |
| Framework            | Spring Boot                             | 4.0.6                    |
| Browser automation   | Selenide                                | 7.2.0                    |
| BDD engine           | Cucumber                                | 7.18.1                   |
| Test runner          | Cucumber JUnit Platform Engine          | 7.18.1                   |
| Test reporting       | Allure                                  | 2.34.0                   |
| Allure integration   | allure-selenide, allure-cucumber7-jvm   | 2.34.0                   |
| AOP weaving          | AspectJ Weaver                          | 1.9.25.1                 |
| Code style           | Checkstyle                              | 3.6.0 (plugin)           |
| Build tool           | Maven                                   | (via wrapper)            |
| Boilerplate reduction| Lombok                                  | (managed by Spring Boot) |

---

## Prerequisites

- **Java 25** (or newer) installed and on `PATH`
- **Google Chrome** installed (default browser)
- Internet access (tests run against `https://www.w3schools.com`)
- No Selenium Grid or WebDriver binary setup needed — Selenide downloads the driver automatically

---

## Code style (Checkstyle)

Checkstyle runs automatically during the **`validate`** phase — before compilation — so the build fails fast on any style violation. The rules are defined in `src/main/checkstyle/checkstyle.xml`.

### What is enforced

| Category         | Rules                                                           |
|------------------|-----------------------------------------------------------------|
| Imports          | No unused imports, no redundant imports                         |
| Modifiers        | Correct modifier order, no redundant modifiers                  |
| Class structure  | Declaration order, one statement per line, no empty statements  |
| Whitespace       | Whitespace around operators/tokens, no trailing whitespace      |
| Curly braces     | Opening brace at end of line, closing brace on same line        |
| File formatting  | Newline at end of file, no multiple trailing blank lines        |
| Correctness      | `equals`/`hashCode` paired, no illegal instantiation           |

### Commands

Run checkstyle only (without compiling or testing):

```bash
./mvnw checkstyle:check
```

Generate an HTML report at `target/reports/checkstyle.html`:

```bash
./mvnw checkstyle:checkstyle
```

Skip checkstyle when needed (e.g. during a WIP build):

```bash
./mvnw test -Dcheckstyle.skip=true
```

---

## Project structure

```
src/
├── main/
│   ├── java/
│   │   └── com/github/paczek224/selenide/
│   │       ├── SelenideApplication.java       # Spring Boot entry point
│   │       ├── config/
│   │       │   └── WebDriverConfig.java       # Maps application.yaml → Selenide config
│   │       └── page/
│   │           ├── AbstractPage.java          # Base class for all page objects
│   │           └── DemoPage.java              # W3Schools HTML Forms page object
│   └── resources/
│       └── application.yaml                   # Browser & URL configuration
└── test/
    ├── java/
    │   └── com/github/paczek224/selenide/
    │       ├── assertions/
    │       │   └── ElementAssertions.java         # Fluent custom assertions
    │       ├── hooks/
    │       │   └── Hooks.java                     # @Before (Allure setup) / @After (teardown + logging)
    │       ├── steps/
    │       │   ├── CucumberSpringBaseTest.java         # Base class wiring Spring context into Cucumber
    │       │   └── FormSteps.java                     # Step definitions for forms.feature
    │       └── suites/
    │           └── RegressionSuiteTest.java        # JUnit Platform Suite — discovers features
    └── resources/
        ├── features/
        │   └── forms.feature                      # Gherkin scenarios
        └── junit-platform.properties              # Cucumber plugins & parallel execution config
```

---

## Configuration

All browser settings live in `src/main/resources/application.yaml` under the `selenide` prefix, which maps directly to `WebDriverConfig` via `@ConfigurationProperties(prefix = "selenide")`:

```yaml
selenide:
  base-url: https://www.w3schools.com
  browser: chrome
  browserSize: "1920x1080"
  headless: false
  timeout: 8000
```

To run tests **headlessly** (e.g. in CI), change `headless: false` to `headless: true`, or pass a system property at runtime:

```bash
./mvnw test -D"selenide.headless"=true
```

### Parallel execution

Scenario-level parallelism is configured in `src/test/resources/junit-platform.properties`:

```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=fixed
cucumber.execution.parallel.config.fixed.parallelism=2
```

Change `parallelism` to increase or decrease the number of concurrent scenarios.

---

## Running the tests

### Run all scenarios

```bash
./mvnw test
```

### Run scenarios by tag

Add a `@tag` to a scenario in the `.feature` file, then filter at runtime:

```bash
./mvnw test -Dcucumber.filter.tags="@smoke"
```

### Run tests headlessly

```bash
./mvnw test -D"selenide.headless"=true
```

### Run tests in a different browser (e.g. Firefox)

```bash
./mvnw test -D"selenide.browser"=firefox
```

---

## Allure report

### Generate and open the report after a test run

```bash
./mvnw allure:serve
```

This builds the report from `target/allure-results/` and opens it in your default browser automatically. Scenarios are grouped by **Feature** and labelled with their Gherkin **Scenario** name.

### Generate a static report (without opening)

```bash
./mvnw allure:report
```

The report is written to `target/site/allure-maven-plugin/`.

---

## Output artifacts

| Path                                | Contents                          |
|-------------------------------------|-----------------------------------|
| `target/allure-results/`            | Raw Allure JSON data              |
| `target/site/allure-maven-plugin/`  | Generated HTML report             |
| `target/reports/screenshots/`       | Screenshots captured by Selenide  |
| `target/downloads/`                 | Files downloaded during tests     |
| `target/surefire-reports/`          | JUnit XML results                 |

---

## How to add a new page and scenario

1. **Create a page object** in `src/main/java/.../page/` extending `AbstractPage`, annotate with `@Component`, declare `SelenideElement` fields in the constructor.
2. **Write a Gherkin scenario** in an existing or new `.feature` file under `src/test/resources/features/`.
3. **Implement step definitions** in a class under `src/test/java/.../steps/` extending `CucumberSpringBaseTest`, inject the page via constructor using `@AllArgsConstructor`.
4. **Use `assertThat(page).elementHasValue(...)`** from `ElementAssertions` for readable assertions.

Example feature file:

```gherkin
Feature: My Feature

  Scenario: User can do X
    Given I open my page
    When I click something
    Then the input should have value "expected"
```

Example step definitions:

```java
@AllArgsConstructor
public class MySteps extends CucumberSpringBaseTest {

    private MyPage myPage;

    @Given("I open my page")
    public void iOpenMyPage() {
        myPage.openPage();
    }

    @When("I click something")
    public void iClickSomething() {
        myPage.clickSomething();
    }

    @Then("the input should have value {string}")
    public void theInputShouldHaveValue(String expected) {
        assertThat(myPage).elementHasValue(MyPage::getSomeInput, expected);
    }
}
```
