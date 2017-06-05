Feature: Regression suit

  Scenario: Simple user bet journey
    Given I am logged in
    When I request my account details
    And I check my account balance
    And I receive prices from competitions
    And I want to bet on the last event from the list
    And I place bet Home team wins "1.00" GBP
    Then I can see bet in history

#Scenario is not finished: bet and bet history validation is missing.
#I can't receive valid response for POST /v1/bets/me