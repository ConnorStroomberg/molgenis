package org.molgenis.selenium.tests.scenario;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HelloWorldTestIT extends TestBaseSetup
{
	private WebDriver webDriver;

	@BeforeClass
	public void setUp() throws Exception
	{
		System.out.println(System.getProperty("browser"));
		System.out.println(System.getProperty("screenshotDirectory"));
		System.out.println(System.getProperty("phantomjs.binary.path"));
		System.out.println(System.getProperty("webdriver.chrome.driver"));
		System.out.println(System.getProperty("screenshotDirectory"));
		
		webDriver = getDriver();
	}

	@Test
	public void helloGoogle()
	{
		webDriver.get("https://www.google.nl");
		assertEquals(webDriver.getTitle(), "Google");
	}

}
