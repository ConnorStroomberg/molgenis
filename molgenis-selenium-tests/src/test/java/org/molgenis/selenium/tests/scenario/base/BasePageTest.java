package org.molgenis.selenium.tests.scenario.base;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.molgenis.selenium.tests.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BasePageTest extends TestBaseSetup
{
	private WebDriver webDriver;

	@BeforeClass
	public void setUp()
	{
		webDriver = getDriver();
	}

	@Test
	public void verifyHomePage() {
		System.out.println("Home page test...");
		BasePage basePage = new BasePage(webDriver);
		Assert.assertTrue(basePage.verifyBasePageTitle(), "Home page title doesn't match");
	}
}
