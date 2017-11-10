package org.molgenis.selenium.tests.scenario.login;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.molgenis.selenium.tests.page.BasePage;
import org.molgenis.selenium.tests.page.LoginPage;
import org.openqa.selenium.WebDriver;
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
//		signInPage = basePage.clickSignInBtn();
//		Assert.assertTrue(signInPage.verifySignInPageTitle(), "Sign In page title doesn't match");
//		Assert.assertTrue(signInPage.verifySignInPageText(), "Page text not matching");
//		Assert.assertTrue(signInPage.verifySignIn(), "Unable to sign in");

	}
}
