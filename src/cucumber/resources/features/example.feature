Feature: Cucumber Tests

  Scenario Outline: Should return 200 OK when calling the healthcheck
    Given the app is running
    When a <http_method> request is sent to <endpoint> with a request body of <request_body> with headers <headers>
    Then a 200 response is returned
    Examples:
      | http_method | endpoint       | request_body | headers   |
      | "GET"       | "/healthcheck" | "empty"      | "headers" |

  Scenario Outline: Should return 200 Unauthorized when contacting an external API
    Given the app is running
    And a <external_response_code> is returned from the external app
    When a <internal_http_method> request is sent to <endpoint> with a request body of <request_body> with headers <headers>
    Then a <internal_response_code> response is returned
    And a <external_http_method> request was made to the external server
    Examples:
      | internal_http_method | external_http_method | endpoint          | request_body | headers   | external_response_code | internal_response_code |
      | "GET"                 | "GET"                | "/fetch/external" | "empty"      | "headers" | 200                    | 200                    |
      | "GET"                 | "GET"                | "/fetch/external" | "empty"      | "headers" | 401                    | 401                    |