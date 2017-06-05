package com.ptrukhanovich.gptest.backend;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features"
)
public class TestWHApi {
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();
}
