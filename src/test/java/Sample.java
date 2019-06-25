import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.*;

public class Sample {

    WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        String driverPath="C:\\Users\\krish.thakkar\\Documents\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver",driverPath);
        driver = new ChromeDriver();
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }


    @Test
    public  void test() {
        String itemID="";
        String itemDesc="";
        Float setQuantity=50.0f;
        String actualitemprice="";
        float finalPrice=0;
        String URL="https://jpetstore.cfapps.io";

        driver.get(URL);
        driver.findElement(By.linkText("Enter the Store")).click();

        List <WebElement>categories  = driver.findElements(By.tagName("a"));
        int el=0;
        Random r = new Random();
        do {
            el = r.nextInt((categories.size()));
            if (el > 3 && el < categories.size())
            {
                categories.get(el).click();
            }
        }
        while (el<4||el>=categories.size()-1);
        List <WebElement> table = driver.findElements(By.xpath("//div[@id='Catalog']//table/tbody/tr/td"));

        do {
            r = new Random();
            el = r.nextInt((table.size()));
            if(el%2==0) {
                table.get(el).findElement(By.tagName("a")).click();

            }
        }
        while (el%2==1);
        itemID = driver.findElement(By.xpath("//*[@id=\"Catalog\"]/table/tbody/tr[2]/td[1]/a")).getText();
        itemDesc= driver.findElement(By.xpath("//tr[2]//td[3]")).getText();
        actualitemprice=(driver.findElement(By.xpath("//tr[2]//td[4]")).getText());
        driver.findElement(By.linkText("Add to Cart")).click();
        driver.findElement(By.id("lines0.quantity")).click();
        driver.findElement(By.id("lines0.quantity")).clear();
        driver.findElement(By.id("lines0.quantity")).sendKeys(Integer.toString(Math.round(setQuantity)));
        driver.findElement(By.name("update")).click();
        Float itemValue= Float.parseFloat(driver.findElement(By.xpath("//*[@id=\"Cart\"]/form/table/tbody/tr[2]/td[6]/span")).getText());
        Float subTotal = itemValue*setQuantity;
        String actualCartVal= (driver.findElement(By.xpath("//td[7]//span[1]")).getText());

        Assert.assertEquals(actualCartVal,new DecimalFormat("##,##,##0.0").format(subTotal)+"0");

        driver.findElement(By.linkText("Proceed to Checkout")).click();
        driver.findElement(By.name("username")).sendKeys("krish1");
        driver.findElement(By.name("password")).sendKeys("krishkrish");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='?'])[1]/following::p[2]")).click();
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("creditCard")).sendKeys("1234554321");
        driver.findElement(By.id("expiryDate")).sendKeys("10/2020");
        driver.findElement(By.name("continue")).click();
        driver.findElement(By.id("order")).click();
        String val = driver.findElement(By.xpath("//li[@class='success']")).getText();

        Assert.assertEquals(val,"Thank you, your order has been submitted.");
        finalPrice=subTotal;

        Assert.assertEquals(driver.findElement(By.xpath("//a/span")).getText(),itemID);
        Assert.assertEquals(driver.findElement(By.xpath("//body//td//td[2]")).getText(),itemDesc);
        Assert.assertEquals((driver.findElement(By.xpath("//body//td[4]")).getText()),actualitemprice);
        Assert.assertEquals(Float.parseFloat(driver.findElement(By.xpath("//body//td[3]")).getText()),setQuantity);
        Assert.assertEquals((driver.findElement(By.xpath("//body//td[5]")).getText()),"$"+new DecimalFormat("##,##,##0.0").format(finalPrice)+0);

        driver.quit();

    }

}