package com.ptrukhanovich.gptest.backend.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

import static org.hamcrest.MatcherAssert.assertThat;

public class StepDefinitions {
    @Steps
    StepImplementation stepImplementation;

    @Given("^I am logged in$")
    public void iAmLoggedIn() {
        assertThat("Authorisation response code is not 201",
                stepImplementation.getAuthorisationResponse().response().getStatusCode() == 201);
    }

    @When("^I request my account details$")
    public void iRequestMyAccountDetails() {
        assertThat("My account details response is not 200",
                stepImplementation.getUserAccountDetails().response().getStatusCode() == 200);
    }

    @And("^I check my account balance$")
    public void iCheckMyAccountBalance() {
        assertThat("My account balance response is not 200",
                stepImplementation.getUserAccountBalance().response().getStatusCode() == 200);

        assertThat("My account balance is low",
                Float.parseFloat(stepImplementation.getUserAccountBalanceValueFromResponse("balance")) > 0);
    }

    @And("^I receive prices from competitions$")
    public void iReceivePricesFromCompetitions() {
        assertThat("Competitions response is not 200",
                stepImplementation.getCompetitions().response().getStatusCode() == 200);
    }

    @And("^I want to bet on the last event from the list$")
    public void iWantToBetOnTheLastEventFromTheList() {
        String lastEventId = stepImplementation.extractLastEventFromAllCompetitions();
        stepImplementation.getEventMarketsOutcomes(lastEventId);
    }

    @And("^I place bet Home team wins \"([^\"]*)\" GBP$")
    public void iPlaceBetHomeTeamWinsGBP(String betAmount) {
        String outcomeId = stepImplementation.getOutcomeIdHomeToWin();

        assertThat("Bet didn't placed",
                stepImplementation.placeBetTo(outcomeId, betAmount).response().getStatusCode() == 200);
    }

    @Then("^I can see bet in history$")
    public void iCanSeeBetInHistory() {
        assertThat("Bet history didn't work",
                stepImplementation.getBetHistory().response().getStatusCode() == 200);
    }
}
