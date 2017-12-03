package org.molgenis.selenium.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	private String getLocalDriverBinaryLocation(String driverName){
		URL resource = TestBaseSetup.class.getResource(File.separator + driverName);
		try
		{
			return Paths.get(resource.toURI()).toAbsolutePath().toString();
		}
		catch (URISyntaxException e)
		{
			System.out.println("Could not find local selenium driver with name " + driverName);
			e.printStackTrace();
		}
		return "";
	}

	private WebDriver initChromeDriver(String appURL) {
		System.out.println("Launching google chrome with new profile..");
		String driverLocation = getLocalDriverBinaryLocation("chromedriver");
		System.setProperty("webdriver.chrome.driver", driverLocation);
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	private WebDriver initFirefoxDriver(String appURL) {
		System.out.println("Launching Firefox browser..");
		String driverLocation = getLocalDriverBinaryLocation("geckodriver");
		System.setProperty("webdriver.gecko.driver", driverLocation);
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
			e.printStackTrace();
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if(driver != null) {
			driver.quit();
		}
	}
}
