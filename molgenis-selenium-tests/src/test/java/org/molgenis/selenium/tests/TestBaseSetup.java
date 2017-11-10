package org.molgenis.selenium.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Map;

public class TestBaseSetup {

	private WebDriver driver;
	private final static Map<String, String> environmentVariables = System.getenv();

	public WebDriver getDriver() {
		return driver;
	}

	private void setDriver(String browserType, String appURL) {
		switch (browserType) {
			case "chrome":
				driver = initChromeDriver(appURL);
				break;
			case "firefox":
				driver = initFirefoxDriver(appURL);
				break;
			default:
				System.out.println("browser : " + browserType
						+ " is invalid, Launching Firefox as browser of choice..");
				driver = initFirefoxDriver(appURL);
		}
	}

	private static WebDriver initChromeDriver(String appURL) {
		System.out.println("Launching google chrome with new profile..");
		String driverPath =  environmentVariables.getOrDefault("selenium.chrome.diver.path", "/Users/connorstroomberg/Downloads/chromedriver");
		System.setProperty("webdriver.chrome.driver", driverPath
				+ "chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	private static WebDriver initFirefoxDriver(String appURL) {
		System.out.println("Launching Firefox browser..");
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}


	@BeforeClass
	public void initializeTestBaseSetup() {
		String browserType = environmentVariables.getOrDefault("selenium.browser.type", "firefox");
		String appURL = environmentVariables.getOrDefault("selenium.app.url", "http://localhost:8080");
		try {
			setDriver(browserType, appURL);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if(driver != null) {
			driver.quit();
		}
	}
}
