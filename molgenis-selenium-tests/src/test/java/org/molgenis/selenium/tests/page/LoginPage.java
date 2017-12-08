package org.molgenis.selenium.tests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

import static org.molgenis.selenium.tests.TestBaseSetup.DEFAULT_TIME_OUT_IN_SECONDS;

public class LoginPage
{
	private static final String PAGE_TITLE = "Login";

	private WebDriver driver;

	private By loginModalLabelSelector = By.id("login-modal-label");
	private By userNameInputSelector = By.id("username-field");
	private By passwordInputSelector = By.id("password-field");
	private By loginBtnSelector = By.id("signin-button");
	private By errorDivSelector = By.id("alert-container");

	private WebElement getNameInput() {
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS))
				.until(ExpectedConditions.presenceOfElementLocated(userNameInputSelector));
	}

	private WebElement getPasswordInput() {
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS))
				.until(ExpectedConditions.presenceOfElementLocated(passwordInputSelector));
	}

	private WebElement getSignInButton() {
		return (new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS))
				.until(ExpectedConditions.presenceOfElementLocated(loginBtnSelector));
	}

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
		WebElement userNameTextInput = getNameInput();
		userNameTextInput.sendKeys(userName);
	}

	public void enterPassword(String password) {
		WebElement passwordTxtBox = getPasswordInput();
		passwordTxtBox.sendKeys(password);
	}

	public void clickOnSignIn() {
		WebElement signInBtn = getSignInButton();
		signInBtn.click();
	}

	public void signIn(String userName, String password)
	{
		enterUserName(userName);
		enterPassword(password);
		clickOnSignIn();
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
