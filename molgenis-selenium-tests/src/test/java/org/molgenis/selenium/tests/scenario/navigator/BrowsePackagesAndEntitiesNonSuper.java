package org.molgenis.selenium.tests.scenario.navigator;

import com.google.common.base.Strings;
import io.restassured.RestAssured;
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
import utils.RestTestUtils;

import static org.slf4j.LoggerFactory.getLogger;
import static utils.RestTestUtils.*;
import static utils.RestTestUtils.Permission.READ;
import static utils.RestTestUtils.Permission.WRITE;

public class BrowsePackagesAndEntitiesNonSuper extends TestBaseSetup
{
	private WebDriver webDriver;

	private static final Logger LOG = getLogger(BrowsePackagesAndEntitiesNonSuper.class);

	private static final String TEST_USER = "browse_pack_and_ent_test_user";
	private static final String TEST_USER_PASSWORD = "browse_pack_and_ent_test_user_password";

	private String adminToken;
	private String testUserToken;
	private String testUserId;

	private LoginPage loginPage;
	private BasePage basePage;
	private NavigatorPage navigatorPage;

	@BeforeClass
	public void setUp()
	{
		webDriver = getDriver();

		LOG.info("Read environment variables");
		String envHost = System.getenv("selenium.app.url");
		RestAssured.baseURI = Strings.isNullOrEmpty(envHost) ? "http://localhost:8080" : envHost;
		LOG.info("baseURI: " + RestAssured.baseURI);

		String envAdminName = System.getProperty("REST_TEST_ADMIN_NAME");
		String adminUserName = Strings.isNullOrEmpty(envAdminName) ? RestTestUtils.DEFAULT_ADMIN_NAME : envAdminName;
		LOG.info("adminUserName: " + adminUserName);

		String envAdminPW = System.getProperty("REST_TEST_ADMIN_PW");
		String adminPassword = Strings.isNullOrEmpty(envAdminPW) ? DEFAULT_ADMIN_PW : envAdminPW;
		LOG.info("adminPassword: " + adminPassword);

		adminToken = login(adminUserName, adminPassword);
		createUser(adminToken, TEST_USER, TEST_USER_PASSWORD);
		testUserId = getUserId(adminToken, TEST_USER);

		grantSystemRights(adminToken, testUserId, "sys_md_Package", WRITE);
		grantSystemRights(adminToken, testUserId, "sys_md_EntityType", WRITE);
		grantSystemRights(adminToken, testUserId, "sys_md_Attribute", WRITE);

		grantSystemRights(adminToken, testUserId, "sys_FileMeta", WRITE);
		grantSystemRights(adminToken, testUserId, "sys_sec_Owned", READ);
		grantSystemRights(adminToken, testUserId, "sys_L10nString", WRITE);

		grantPluginRights(adminToken, testUserId, "navigator");

		testUserToken = login(TEST_USER, TEST_USER_PASSWORD);
	}

	@Test
	public void openNavigator()
	{

		basePage = new BasePage(webDriver);
		basePage.clickOnSignIn();

		loginPage = new LoginPage(webDriver);
		loginPage.signIn(TEST_USER, TEST_USER_PASSWORD);
		Assert.assertTrue(basePage.verifySignOutBtn(), "Expected sign-out btn not visible, was login successful ?");

		basePage.clickOnNavigatorMenuItem();

		navigatorPage = new NavigatorPage(webDriver);
		Assert.assertTrue(navigatorPage.verifyPackageLinkWithName("Default"),
				"Expected Package link with name \"Default\" to be percent");
	}

	@AfterClass(alwaysRun = true)
	public void afterClass()
	{
		// Clean up permissions
		removeRightsForUser(adminToken, testUserId);

		// Clean up Token for user
		cleanupUserToken(testUserToken);

		// Clean up user
		cleanupUser(adminToken, testUserId);
	}
}
