package org.molgenis.selenium.tests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;

public class LoginPage
{
	private static final String PAGE_TITLE = "Login";

	private WebDriver driver;

	private By loginModalLabelSelector = By.id("login-modal-label");
	private By userNameInputSelector = By.id("username-field");
	private By passwordInputSelector = By.id("password-field");
	private By loginBtnSelector = By.id("signin-button");
	private By errorDivSelector = By.id("alert-container");


	public LoginPage(WebDriver driver)
	{
		this.driver = driver;
	}

	public String loginPageTitle() {
		return driver.getTitle();
	}

	public boolean verifyLoginPageTitle() {
		return loginPageTitle().contains(PAGE_TITLE);
	}

	public boolean verifyLoginPageLabel() {
		WebElement element = driver.findElement(loginModalLabelSelector);
		String pageText = element.getText();
		String expectedPageText = "Sign in";
		return pageText.contains(expectedPageText);
	}

	public void enterUserName(String userName) {
		WebElement userNameTextInput = driver.findElement(this.userNameInputSelector);
		userNameTextInput.sendKeys(userName);
	}

	public void enterPassword(String password) {
		WebElement passwordTxtBox = driver.findElement(passwordInputSelector);
		passwordTxtBox.sendKeys(password);
	}

	public void clickOnSignIn() {
		WebElement signInBtn = driver.findElement(loginBtnSelector);
		signInBtn.click();
	}

	@Nullable
	public String getErrorMessage() {
		String strErrorMsg = null;
		WebElement errorMsg = driver.findElement(errorDivSelector);
		if(errorMsg != null && errorMsg.isDisplayed()) {
			strErrorMsg = errorMsg.getText();
		}
		return strErrorMsg;
	}
}
