package com.ptrukhanovich.gptest.backend.steps;

import com.ptrukhanovich.gptest.backend.steps.autowired.RequestSpecifications;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.annotations.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RequestSpecifications.class})
public class StepImplementation {
    private static final String PATH_TO_BALANCE_VALUES = "whoAccounts.account.";
    private static final String PATH_TO_AUTH_TICKET = "*.ticket";
    private static final String LAST_EVENT_PATH = "whoCompetitions.category.class.type[-1].event[-1].id";
    private static final String CONCRETE_OUTCOME_MASK = "whoCompetitions.event.market[%s].outcome[%s].id";
    private static final String HEADER_TICKET = "who-ticket";

    @Autowired
    private RequestSpecification sessionApiSpec;

    @Autowired
    private RequestSpecification accountsApiSpec;

    @Autowired
    private RequestSpecification balanceApiSpec;

    @Autowired
    private RequestSpecification competitionsApiSpec;

    @Autowired
    private RequestSpecification eventOutcomesApiSpec;

    @Autowired
    private RequestSpecification betslipApiSpec;

    @Autowired
    private RequestSpecification betHistoryApiSpec;

    private ExtractableResponse<Response> authorisationResponse;
    private ExtractableResponse<Response> userAccountDetails;
    private ExtractableResponse<Response> userAccountBalance;
    private ExtractableResponse<Response> competitions;
    private ExtractableResponse<Response> eventOutcomes;
    private ExtractableResponse<Response> betslip;


    private String authTicket = "";


    @Step
    public ExtractableResponse<Response> getAuthorisationResponse() {
        if (this.authorisationResponse == null || this.authTicket.equals("")) {
            this.authorisationResponse = retrieveAuthorisationResponse();
            this.authTicket = this.authorisationResponse.response().getBody().xmlPath().getString(PATH_TO_AUTH_TICKET);
        }

        return this.authorisationResponse;
    }

    @Step
    private ExtractableResponse<Response> retrieveAuthorisationResponse() {
        return RestAssured.given()
                .spec(sessionApiSpec)
                .post()
                .then().log().all().extract();
    }

    @Step
    public ExtractableResponse<Response> getUserAccountDetails() {
        userAccountDetails = RestAssured.given()
                                .spec(accountsApiSpec.header(HEADER_TICKET, this.authTicket))
                                .log().all()
                                .get()
                                .then().log().all().extract();

        return userAccountDetails;
    }

    @Step
    public ExtractableResponse<Response> getUserAccountBalance() {
        userAccountBalance = RestAssured.given()
                                .spec(balanceApiSpec.header(HEADER_TICKET, this.authTicket))
                                .log().all()
                                .get()
                                .then().log().all().extract();

        return userAccountBalance;
    }

    @Step
    public String getUserAccountBalanceValueFromResponse(String nodeName) {
        return userAccountBalance.response().getBody().xmlPath().getString(PATH_TO_BALANCE_VALUES + nodeName);
    }

    @Step
    public ExtractableResponse<Response> getCompetitions() {
        competitions = RestAssured.given()
                .spec(competitionsApiSpec)
                .log().all()
                .get()
                .then().log().all().extract();

        return competitions;
    }

    @Step
    public String extractLastEventFromAllCompetitions() {
        return JsonPath.with(this.competitions.response().asString())
                .getString(LAST_EVENT_PATH);
    }

    @Value("${urlMaskToEventMarket}")
    private String eventOutcomesApiUrlMask;

    @Step
    public ExtractableResponse<Response> getEventMarketsOutcomes(String eventId) {
        eventOutcomes = RestAssured.given()
                .spec(eventOutcomesApiSpec.baseUri(String.format(eventOutcomesApiUrlMask, eventId)))
                .log().all()
                .get()
                .then().log().all().extract();

        return eventOutcomes;
    }

    @Step
    public String getOutcomeIdHomeToWin() {
        int marketIndex = 0;
        int outcomeIndex = 0;
        return JsonPath.with(this.eventOutcomes.response().asString())
                .getString(String.format(CONCRETE_OUTCOME_MASK, marketIndex, outcomeIndex));

    }

    @Step
    public ExtractableResponse<Response> placeBetTo(String outcomeId, String betAmount) {
        betslip = RestAssured.given()
                .spec(betslipApiSpec.header(HEADER_TICKET, this.authTicket))
                .queryParam("legType", "W")
                .queryParam("stake", betAmount)
                .queryParam("outcomeId", outcomeId)
                .queryParam("priceType", "L")
                .log().all()
                .post()
                .then().log().all().extract();

        return betslip;
    }

    @Step
    public ExtractableResponse<Response> getBetHistory() {
        betslip = RestAssured.given()
                .spec(betHistoryApiSpec.header(HEADER_TICKET, this.authTicket))
                .queryParam("blockSize", "5")
                .queryParam("blockNum", "0")
                .log().all()
                .get()
                .then().log().all().extract();

        return betslip;
    }
}
