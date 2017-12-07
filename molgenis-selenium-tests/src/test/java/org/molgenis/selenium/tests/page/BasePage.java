package org.molgenis.selenium.tests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage
{
	private static final String PAGE_TITLE = "MOLGENIS";
	private WebDriver driver;

	private By signInBtnSelector = By.id("open-button");
	private By signOutBtnSelector = By.id("signout-button");
	private By navigatorMenuItemSelector = By.linkText("Navigator");


	public BasePage(WebDriver driver)
	{
		this.driver = driver;
	}

	public String getPageTitle(){
		return driver.getTitle();
	}

	public WebElement getSignInBtn() {
		return (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(signInBtnSelector));
	}

	public WebElement getSignOutBtn() {
		return (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(signOutBtnSelector));
	}

	public WebElement getNavigatorMenuItem() {
		return (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(navigatorMenuItemSelector));
	}

	public boolean verifyBasePageTitle() {
		return getPageTitle().contains(PAGE_TITLE);
	}

	public boolean verifySignInBtn() {
		return getSignInBtn().isDisplayed();
	}

	public boolean verifySignOutBtn() {
		return getSignOutBtn().isDisplayed();
	}


	public void clickOnSignIn() {
		getSignInBtn().click();
	}

	public void clickOnNavigatorMenuItem() {
		getNavigatorMenuItem().click();
	}
}
