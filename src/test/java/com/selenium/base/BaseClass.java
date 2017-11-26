package com.selenium.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseClass implements ITestListener{
	
	public static WebDriver driver;
	
	
	public static Logger logger = Logger.getLogger("BaseClass");
//*****************************************************************
	
	public static void writeLogs(String msg)
	{
		logger.info(msg);
	}
	
	public static void writeErrorLogs( Throwable t)
	{
		String s = Arrays.toString(t.getStackTrace());
		
		logger.error(s.replaceAll(",","\n"));
	}
	
	/** 
	 * This method is used for fetching the config data 
	 * @param propName
	 * @return String property value
	 * @throws IOException
	 */
	
	
	public static String getConfigData(String propName) throws IOException
	{
		String propValue = null;
		
		File f = new File("./src/test/data/config.properties");
		InputStream fio = new FileInputStream(f);
		
		Properties prop = new Properties();
		prop.load(fio);
		
		propValue = 	prop.getProperty(propName);
		
		
		
		return propValue;
	}
	
//******************************************************************
	
	public static String getTestData(String propName) throws IOException
	{
		String propValue = null;
		
		File f = new File("./src/test/data/testdata.properties");
		InputStream fio = new FileInputStream(f);
		
		Properties prop = new Properties();
		prop.load(fio);
		
		propValue = 	prop.getProperty(propName);
		
		
		
		return propValue;
	}
	
	
	public static String getLocatorData(String propName) throws IOException
	{
		String propValue = null;
		
		File f = new File("./src/test/data/locatordata.properties");
		InputStream fio = new FileInputStream(f);
		
		Properties prop = new Properties();
		prop.load(fio);
		
		propValue = 	prop.getProperty(propName);
		
		
		
		return propValue;
	}
	
	
	
	@BeforeMethod(alwaysRun=true)
	public static void startSelenium() throws IOException
	{
		writeLogs("This method will run before every method");
		String browser = getConfigData("browser");
		String url = getConfigData("url");
		
		if (browser.equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "./src/test/Utilities/geckodriver.exe");
			driver = new FirefoxDriver();								
			
		}
		else if(browser.equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "./src/test/Utilities/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		
			
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		
		driver.get(url);
		
			
	}
	
	@AfterMethod(alwaysRun=true)
	public static void closeSelenium() throws InterruptedException
	{
		writeLogs("This method will run after every method");
		
		driver.quit();
		
		WindowsUtils.killByName("chromedriver.exe");
		Thread.sleep(4000);
	
	}
	
	
	
	public static void writeResults(String testcaseName, String status) throws IOException
	{
		File f = new File("./src/test/results/results.txt");
		
		FileWriter fw = new FileWriter(f,true);
		
		fw.write(testcaseName +"--" +status +"\r\n");
		
		fw.flush();
		fw.close();
		
		
		
	}
	
	public static void captureScreenShot(String fileName) throws IOException
	{
		
		File dest =  new File("./src/test/results/screenshots/"+fileName+".png");	
		File src = 	((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			
		//org.apache.commons.io.FileUtils.copyFile(src, dest);
		
		FileUtils.copyFile(src, dest);
		
	}
	
	
	@BeforeSuite(alwaysRun=true)
	public static void beforeSuite()
	{
		writeLogs("This method will run before everything");
		WindowsUtils.killByName("firefox.exe");
	}

	
	@AfterSuite(alwaysRun=true)
	public static void afterSuite()
	{
		writeLogs("This method will run after everything");
	}
	
	@BeforeTest(alwaysRun=true)
	public static void beforeTest()
	{
		writeLogs("This method will run before every TestNG test in the xml file");
	}
	
	@AfterTest(alwaysRun=true)
	public static void afterTest()
	{
		writeLogs("This method will run after every TestNG test in the xml file");
	}
	
	
	@BeforeClass(alwaysRun=true)
	public static void beforeClass()
	{
		writeLogs("This method will run before every class");
	}
	
	@AfterClass(alwaysRun=true)
	public static void afterClass()
	{
		writeLogs("This method will run after every class");
	}

	
	//=============================
	
	
	public void onFinish(ITestContext arg0) {
		writeLogs("######This method runs at the end of execution######");
	}

	
	public void onStart(ITestContext arg0) {
		writeLogs("######This method runs at the begining of execution######");
		
	}


	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
		
	}

	
	public void onTestFailure(ITestResult arg0) {
		
		
		
		writeErrorLogs(arg0.getThrowable());
		
		try {
			writeResults(arg0.getName(), "Fail");
			captureScreenShot(arg0.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	
	public void onTestSkipped(ITestResult arg0) {
		writeLogs("The test case by name "+arg0.getName() + " is skipped !!!");
		
	}

	
	public void onTestStart(ITestResult arg0) {
		
		writeLogs("***********The test case by name "+arg0.getName() + " is getting started*******");
				
	}

	
	public void onTestSuccess(ITestResult arg0) {		
			writeLogs("The test case by name "+arg0.getName() + " is Pass");
		
		try {
			writeResults(arg0.getName(), "PASS");
			captureScreenShot(arg0.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
	