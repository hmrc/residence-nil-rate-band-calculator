Feature: Hello World

  Scenario: Access the Hello World endpoing
    When I GET the hello-world endpoint
    Then I should get an OK response
