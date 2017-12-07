package org.molgenis.selenium.tests.scenario.login;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.molgenis.selenium.tests.page.BasePage;
import org.molgenis.selenium.tests.page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AdminLoginTest extends TestBaseSetup
{
	private WebDriver webDriver;
	private LoginPage loginPage;
	private BasePage basePage;

	@BeforeClass
	public void setUp()
	{
		webDriver = getDriver();
	}

	@Test
	public void verifyLogin() {
		System.out.println("Login In functionality details...");
		basePage = new BasePage(webDriver);

		basePage.clickOnSignIn();
		loginPage = new LoginPage(webDriver);

		loginPage.enterUserName("admin");
		loginPage.enterPassword("admin");
		loginPage.clickOnSignIn();

		Assert.assertTrue(basePage.verifySignOutBtn(), "Expected sign-out btn not visible, was login successful ?");

	}
}
