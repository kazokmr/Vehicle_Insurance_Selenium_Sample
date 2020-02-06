package e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VehicleInsuranceTest {
  private WebDriver webDriver;
  
  @BeforeEach
  void createDriver() {
    System.setProperty("webdriver.chrome.driver", "driver/chrome/mac/chromedriver");
    //    System.setProperty("webdriver.chrome.driver", "driver/chrome/windows/chromedriver.exe");
    webDriver = new ChromeDriver();
  }
  
  @AfterEach
  void tearDown() {
    webDriver.close();
  }
  
  @Test
  void insuranceQuoteTest() {
    openInsurancePage();
    enterVehicleData();
    enterInsurantData();
    enterProductData();
    
    SoftAssertions priceTable = new SoftAssertions();
    priceTable.assertThat(webDriver.findElement(By.id("selectsilver_price")).getText()).as("SilverPlanの保険料").isEqualTo("299.00");
    priceTable.assertThat(webDriver.findElement(By.id("selectgold_price")).getText()).as("GoldPlanの保険料").isEqualTo("880.00");
    priceTable.assertThat(webDriver.findElement(By.id("selectplatinum_price")).getText()).as("PlatinumPlanの保険料").isEqualTo("1,728.00");
    priceTable.assertThat(webDriver.findElement(By.id("selectultimate_price")).getText()).as("UltimatePlanの保険料").isEqualTo("3,292.00");
    
    ((JavascriptExecutor) webDriver).executeScript("document.getElementById('selectgold').click();");
    
    new WebDriverWait(webDriver, 3L).until(ExpectedConditions.elementToBeClickable(By.id("nextsendquote")));
    webDriver.findElement(By.id("nextsendquote")).click();
    
    assertThat(webDriver.getTitle()).as("ページタイトルの確認").isEqualTo("Send Quote");
  }
  
  private void enterProductData() {
    webDriver.findElement(By.id("startdate")).sendKeys("04/01/2020");
    new Select(webDriver.findElement(By.id("insurancesum"))).selectByValue("10000000");
    new Select(webDriver.findElement(By.id("meritrating"))).selectByValue("Bonus 1");
    new Select(webDriver.findElement(By.id("damageinsurance"))).selectByValue("Partial Coverage");
    ((JavascriptExecutor) webDriver).executeScript("document.getElementById('LegalDefenseInsurance').click();");
    new Select(webDriver.findElement(By.id("courtesycar"))).selectByValue("No");
    webDriver.findElement(By.id("nextselectpriceoption")).click();
  }
  
  private void enterInsurantData() {
    webDriver.findElement(By.id("firstname")).sendKeys("Rickma");
    webDriver.findElement(By.id("lastname")).sendKeys("RS");
    webDriver.findElement(By.id("birthdate")).sendKeys("01/04/2000");
    new Select(webDriver.findElement(By.id("country"))).selectByValue("Japan");
    webDriver.findElement(By.id("zipcode")).sendKeys("1000004");
    new Select(webDriver.findElement(By.id("occupation"))).selectByValue("Employee");
    // チェックボックスをクリックするとspanタグが重なっているのでElement is not clickable at pointが発生する。JSで実行している
    ((JavascriptExecutor) webDriver).executeScript("document.getElementById('speeding').click();");
    webDriver.findElement(By.id("nextenterproductdata")).click();
  }
  
  private void enterVehicleData() {
    Select selectMake = new Select(webDriver.findElement(By.id("make")));
    selectMake.selectByValue("Toyota");
    webDriver.findElement(By.id("engineperformance")).sendKeys("500");
    webDriver.findElement(By.id("dateofmanufacture")).sendKeys("02/06/2020");
    new Select(webDriver.findElement(By.id("numberofseats"))).selectByValue("7");
    new Select(webDriver.findElement(By.id("fuel"))).selectByValue("Gas");
    webDriver.findElement(By.id("listprice")).sendKeys("10000");
    webDriver.findElement(By.id("annualmileage")).sendKeys("5000");
    webDriver.findElement(By.id("nextenterinsurantdata")).click();
  }
  
  private void openInsurancePage() {
    webDriver.manage().window().maximize();
    webDriver.get("http://sampleapp.tricentis.com/101/index.php");
    webDriver.findElement(By.id("nav_automobile")).click();
  }
}
