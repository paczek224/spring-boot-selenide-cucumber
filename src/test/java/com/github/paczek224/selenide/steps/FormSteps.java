package com.github.paczek224.selenide.steps;

import com.github.paczek224.selenide.page.DemoPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.AllArgsConstructor;

import static com.github.paczek224.selenide.assertions.ElementAssertions.assertThat;

@AllArgsConstructor
public class FormSteps extends CucumberSpringBaseTest {

    private DemoPage demoPage;

    @Given("I open the demo page")
    public void iOpenTheDemoPage() {
        demoPage.openPage();
    }

    @When("I fill the first name with {string}")
    public void iFillTheFirstNameWith(String value) {
        demoPage.fillFirstName(value);
    }

    @When("I fill the last name with {string}")
    public void iFillTheLastNameWith(String value) {
        demoPage.fillLastName(value);
    }

    @Then("the first name field should have value {string}")
    public void theFirstNameFieldShouldHaveValue(String expected) {
        assertThat(demoPage).elementHasValue(DemoPage::getFirstNameInput, expected);
    }

    @Then("the last name field should have value {string}")
    public void theLastNameFieldShouldHaveValue(String expected) {
        assertThat(demoPage).elementHasValue(DemoPage::getLastNameInput, expected);
    }
}
