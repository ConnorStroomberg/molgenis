package org.molgenis.selenium.tests.page;

import org.openqa.selenium.WebDriver;

public class BasePage
{
	private static final String PAGE_TITLE = "MOLGENIS";
	private WebDriver driver;

	public BasePage(WebDriver driver)
	{
		this.driver = driver;
	}

	public String getPageTitle(){
		return driver.getTitle();
	}

	public boolean verifyBasePageTitle() {
		return getPageTitle().contains(PAGE_TITLE);
	}
}
