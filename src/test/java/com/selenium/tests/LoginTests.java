package com.selenium.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.selenium.base.BaseClass;

public class LoginTests extends BaseClass{
	
	@Test
	public static void login_001() throws Exception
	{
		// Loging in to the actitime application
		driver.findElement(By.xpath(getLocatorData("UserName_EditBox"))).sendKeys(getTestData("UserName_EditBox"));
		
		driver.findElement(By.xpath(getLocatorData("Password_EditBox"))).sendKeys(getTestData("Password_EditBox"));
		
		
		driver.findElement(By.xpath(getLocatorData("Login_Button"))).click();
		
		Thread.sleep(5000);
		
		boolean result = driver.findElement(By.xpath(getLocatorData("Logout_Link"))).isDisplayed();
				
		
		Assert.assertTrue(result, "could not login to actitime");
		
		driver.findElement(By.xpath(getLocatorData("Logout_Link"))).click();
		
		
	}
	
	
	@Test
	public static void InvalidLogin_001() throws Exception
	{
		// Loging in to the actitime application
		driver.findElement(By.xpath(getLocatorData("UserName_EditBox"))).sendKeys(getTestData("UserName_EditBox"));
		
		driver.findElement(By.xpath(getLocatorData("Password_EditBox"))).sendKeys(getTestData("Password_EditBox")+"invalid");
		
		
		driver.findElement(By.xpath(getLocatorData("Login_Button"))).click();
		
		Thread.sleep(5000);
		
		boolean result = false;
		
		try {
			
			result = driver.findElement(By.xpath(getLocatorData("UserName_EditBox"))).isDisplayed();
			
		}
		catch(Exception e)
		{
			writeLogs("Logged in to actitime...expected  to fail");
		}				
		
		// Adding a comment to check the build trigger by jenkins
		Assert.assertTrue(result, "Logged in to actitime, expected login to fail");
		
		driver.findElement(By.xpath(getLocatorData("Logout_Link"))).click();
		
		
		
		
	}


}
