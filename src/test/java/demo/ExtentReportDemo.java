package demo;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportDemo {
	
	public static ExtentHtmlReporter htmlreport;
	public static ExtentReports extent;
	public static ExtentTest  test;
	public WebDriver driver;
	
	
	@BeforeMethod
	public void reportSetup()
	{
		htmlreport=new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/myReport1.html");
		htmlreport.config().setReportName("ExtentReport");
		htmlreport.config().setDocumentTitle("Test Case Execution");
		htmlreport.config().setTheme(Theme.DARK);
		//htmlreport.config().
		
		extent=new ExtentReports();
		extent.attachReporter(htmlreport);
		extent.setSystemInfo("OS", "Windows 7");
		extent.setSystemInfo("QA Name", "Laxman");
		extent.setSystemInfo("Html Report", "Test Output Folder");
		
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		//JavascriptExecutor js = (JavascriptExecutor)driver;
		
		
	}
	
	@Test
	public void collectLinks()
	{
		driver.get("https://www.google.co.in/");
		test=extent.createTest("collectLinks");
		List<WebElement> gLinks=driver.findElements(By.xpath("//a[@href]"));
		System.out.println("Links Count is "+gLinks.size());
		String act=driver.getTitle();
		int f=29;
		for (int i = 0; i < gLinks.size(); i++) {
			
			WebElement elelink=gLinks.get(i);
			String link=elelink.getAttribute("href");
			
			verifyLink(link);
		}
		
		String titlerxp="Google";
		Assert.assertEquals(act, titlerxp);
		
	}
	
	public void verifyLink(String hlink)
	{
		try 
        {
           URL url = new URL(hlink);
           
           HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
           
           httpURLConnect.setConnectTimeout(3000);
           
           httpURLConnect.connect();
           
           if(httpURLConnect.getResponseCode()==200)
           {
               System.out.println(hlink+" - "+httpURLConnect.getResponseMessage());
            }
          if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND)  
           {
               System.out.println(hlink+" - "+httpURLConnect.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
            }
        } catch (Exception e) {
           
        }
	}
	
	@Test
	public void scrollDemo() throws InterruptedException
	{
		test=extent.createTest("scrollDemo");
		driver.get("https://jqueryui.com/");
		Thread.sleep(4000);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("scroll(0,400)");
		boolean stst=true;
		
		Assert.assertTrue(stst);
		
	}
	
	@Test
	public void scrollView() throws InterruptedException
	{
		test=extent.createTest("scrollView");
		 driver.get("http://demo.guru99.com/test/guru99home/");
		Thread.sleep(4000);
		
		WebElement Element = driver.findElement(By.linkText("Linux"));
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", Element);
		Thread.sleep(7000);
		System.out.println("Element text "+Element.getText());
		Assert.assertEquals(Element.getText(),	"Linux");
		
		
	}
	
	
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException
	{
		if(result.getStatus() == ITestResult.FAILURE)
		{
			//MarkupHelper is used to display the output in different colors
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));

			//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
			//We do pass the path captured by this method in to the extent reports using "logger.addScreenCapture" method. 

			//	String Scrnshot=TakeScreenshot.captuerScreenshot(driver,"TestCaseFailed");
			String screenshotPath = TakeScreenshot(driver, result.getName());
			//To add it in the extent report 

			test.fail("Test Case Failed Snapshot is below " + test.addScreenCaptureFromPath(screenshotPath));


		}
		else if(result.getStatus() == ITestResult.SKIP){
			//logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE)); 
		} 
		else if(result.getStatus() == ITestResult.SUCCESS)
		{
			test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
		}
		driver.quit();
		
}
	
	public static String TakeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		// after execution, you could see a folder "FailedTestsScreenshots" under src folder
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
	
	
	
	
	@AfterClass
	public void generateReport()
	
	{  
		extent.flush();
		
		System.out.println("**************END******************");
		
		
	}
	
}