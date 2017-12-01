package org.molgenis.selenium.tests;

import org.molgenis.selenium.tests.config.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestBaseSetup {
	
	private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<DriverFactory>());
	private static ThreadLocal<DriverFactory> driverFactory;

	@BeforeSuite(alwaysRun = true)
	public static void instantiateDriverObject() {
		driverFactory = new ThreadLocal<DriverFactory>() {
			@Override
			protected DriverFactory initialValue() {
				DriverFactory driverFactory = new DriverFactory();
				webDriverThreadPool.add(driverFactory);
				return driverFactory;
			}
		};
	}

	public static WebDriver getDriver() throws Exception {
		return driverFactory.get().getDriver();
	}

	@AfterMethod(alwaysRun = true)
	public static void clearCookies() throws Exception {
		getDriver().manage().deleteAllCookies();
	}

	@AfterSuite(alwaysRun = true)
	public static void closeDriverObjects() {
		for (DriverFactory driverFactory : webDriverThreadPool) {
			driverFactory.quitDriver();
		}
	}


}
