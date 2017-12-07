package org.molgenis.selenium.tests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigatorPage
{
	private WebDriver driver;

	private By navigatorTableSelector = By.tagName("table");

	public NavigatorPage(WebDriver driver)
	{
		this.driver = driver;
	}

	public WebElement getNavigatorTable()
	{
		return (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(navigatorTableSelector));
	}

	public WebElement getPackageLinkByText(String packageLinkText)
	{
		return (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.linkText(packageLinkText)));
	}

	public boolean verifyPackageLinkWithName(String name)
	{
		WebElement link = getPackageLinkByText(name);
		return link.isDisplayed();
	}
}
