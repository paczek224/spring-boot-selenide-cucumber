package com.github.paczek224.selenide.hooks;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Hooks {

    private static final String SEPARATOR = "------------------------------------------------";
    private static volatile boolean allureInitialized = false;

    @Before
    public void setUp() {
        if (!allureInitialized) {
            SelenideLogger.addListener("allure", new AllureSelenide()
                    .screenshots(true)
                    .savePageSource(false));
            allureInitialized = true;
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("{}: {} {}", scenario.getName(), scenario.getStatus(), SEPARATOR);
        WebDriverRunner.closeWebDriver();
    }
}
