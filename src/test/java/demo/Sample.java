package demo;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Sample {
	
	Logger logger=Logger.getLogger(Sample.class);
	ChromeDriver driver;
	
	
	String fpath="C:\\Users\\admin\\My-Eclipse-Workspace\\Seleniumbasics\\log4j.properties";
	//PropertyConfigurator.configure(fpath);
	@Test
	public void driverDetup() throws Exception
	{
		//PropertyConfigurator.configure("C:\\Users\\admin\\My-Eclipse-Workspace\\com.mvntestNG\\src\\main\\resources\\log4j.properties");
		//System.setProperty("webdriver.chrome.driver", "F://Software//Crome//chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		//JavascriptExecutor js = (JavascriptExecutor)driver;
		driver.get("https://www.google.co.in/");
		logger.info("Browser has been launched");
		driver.manage().window().maximize();
		System.out.println("*********Wel-Come************");
		
		WebElement sigin=driver.findElement(By.xpath("//div//a[text()='Sign in']"));
		//safeJavaScriptClick(sigin);
		//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		WebElement tctBox=driver.findElement(By.cssSelector("input[title='Search']"));
		logger.info("Google search txtbox enabled");
		tctBox.sendKeys("Testing tools");
		tctBox.sendKeys(Keys.ENTER);
		logger.info("Text enetred into txt box");
		driver.close();
	}

	@Test 
	public void add()
	{
		logger.info("Add Method is called");
		int a=20,b=30;
		int c=a+b;
		System.out.println("Result of addition is "+c);
	}
	
	@Test 
	public void sub()
	{
		logger.info("Sub Method is called");
		System.out.println("***********************OP************************");
		int a=70,b=30;
		int c=a+b;
		System.out.println("**Result of sub is "+c);
	}
}
