package org.molgenis.selenium.tests.scenario;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HelloWorldTest extends TestBaseSetup
{
	private WebDriver webDriver;

	@BeforeClass
	public void setUp()
	{
		webDriver = getDriver();
	}

	@Test
	public void helloGoogle()
	{
		webDriver.get("https://www.google.nl");
		assertEquals(webDriver.getTitle(), "Google");
	}

}
