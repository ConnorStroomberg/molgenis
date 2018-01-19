package org.molgenis.selenium.tests.scenario.navigator;

import org.molgenis.selenium.tests.TestBaseSetup;
import org.molgenis.selenium.tests.page.BasePage;
import org.molgenis.selenium.tests.page.LoginPage;
import org.molgenis.selenium.tests.page.NavigatorPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class BrowsePackagesAndEntitiesAsSuperUser extends TestBaseSetup
{
	private WebDriver webDriver;

	private static final Logger LOG = getLogger(BrowsePackagesAndEntitiesAsSuperUser.class);

	private LoginPage loginPage;
	private BasePage basePage;
	private NavigatorPage navigatorPage;

	@BeforeClass
	public void setUp()
	{
		webDriver = getDriver();
		basePage = new BasePage(webDriver);
		basePage.clickOnSignIn();

		loginPage = new LoginPage(webDriver);
		loginPage.signIn("admin", "admin");
		Assert.assertTrue(basePage.verifySignOutBtn());
		// Go to the Navigator
		basePage.clickOnNavigatorMenuItem();
		navigatorPage = new NavigatorPage(webDriver);
	}

	@Test
	public void browse()
	{

		//A list off packages and entities should be shown at the root level
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("Default"));
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("System"));

		//'click' the 'System' package
		navigatorPage.followPackageLink("System");

		// The list of packages and entities that are defined within the system package should been shown.
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("Jobs"));
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("Mail"));
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("Meta"));

		//The 'System' package should be added to the breadcrumb path
		List<String> path = Collections.singletonList("System");
		Assert.assertTrue(navigatorPage.verifyBreadCrumbPath(path));
	}

	@AfterClass(alwaysRun = true)
	public void afterClass()
	{

	}
}
