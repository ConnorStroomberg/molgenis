# Selenium integration tests

Selenium tests use a 'real' browser to test user scenario's against a 'live' server.

To run the tests activate the `selenium` maven profile

### The following environment settings are needed during test setup:

* `selenium.browser.type` = [remote|chrome|firefox] (defaults to remote)
* `selenium.app.url` = (defaults to http://localhost:8080)
* `selenium.sauce.user` = (no default, not needed for local test)
* `selenium.sauce.key` = (no default, not needed for local test)

For placing the system into a state at the start of a scenario ( and cleanup after):
* `REST_TEST_ADMIN_NAME` = (defaults to admin)
* `REST_TEST_ADMIN_PW` = (defaults to admin)

### Running test from a local server

* Set `selenium.browser.type` to `chrome` or `firefox`
* Download the driver for the browser and OS you want to test, and place the binary in the test resource folder of the molgenis-selenium-tests module. 
* Set `selenium.app.url` the server you want to test against ( for example `http://localhost:8080`)

### Running test remotely using sauselabs

(to do explain about keys and settings)