package org.molgenis.selenium.tests.page;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

import static org.molgenis.selenium.tests.TestBaseSetup.DEFAULT_TIME_OUT_IN_SECONDS;

public class NavigatorPage
{
	private WebDriver driver;

	private By navigatorTableSelector = By.tagName("table");
	private By breadCrumbsSelector = By.cssSelector(".navigator-path .breadcrumb-item a");

	public NavigatorPage(WebDriver driver)
	{
		this.driver = driver;
	}

	public WebElement getNavigatorTable()
	{
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS)).until(
				ExpectedConditions.presenceOfElementLocated(navigatorTableSelector));
	}

	private WebElement getPackageLinkByText(String packageLinkText)
	{
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS)).until(
				ExpectedConditions.presenceOfElementLocated(By.linkText(packageLinkText)));
	}

	private List<WebElement> getBreadCrumbLinks()
	{
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS)).until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(breadCrumbsSelector));
	}

	public boolean verifyPackageLinkWithName(String name)
	{
		WebElement link = getPackageLinkByText(name);
		return link.isDisplayed();
	}

	public boolean verifyBreadCrumbPath(List<String> crumbTexts)
	{
		List<WebElement> breadCrumbLinks = getBreadCrumbLinks();

		List<String> actualTexts = breadCrumbLinks.stream()
												  .map(WebElement::getText)
												  .filter(StringUtils::isNotEmpty) // home item
												  .collect(Collectors.toList());

		boolean verified = true;
		for (int index = 0; index < crumbTexts.size(); index++)
		{
			if (!crumbTexts.get(index).equals(actualTexts.get(index)))
			{
				verified = false;
			}
		}

		return verified;
	}

	public void followPackageLink(String linkName)
	{
		WebElement link = getPackageLinkByText(linkName);
		link.click();
	}
}
