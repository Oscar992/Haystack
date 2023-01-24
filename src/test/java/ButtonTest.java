import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v107.fetch.Fetch;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Optional;

public class ButtonTest {
    @Test
    public void buttonTest() {
        ChromeDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        DevTools devTool = driver.getDevTools();
        devTool.createSession();

        devTool.send(Network.enable(Optional.empty(), Optional.empty(),
                Optional.empty()));

        devTool.addListener(Network.responseReceived(), responseReceieved -> {

            System.out.println("Response Url => " + responseReceieved.getResponse().getUrl());

            System.out.println("Response Status => " + responseReceieved.getResponse().getStatus());
        });

        devTool.send(Fetch.enable(Optional.empty(), Optional.empty()));

        devTool.addListener(Fetch.requestPaused(), req -> {
            System.out.println("Request URL =>" + req.getRequest().getUrl());
            devTool.send(Fetch.continueRequest(req.getRequestId(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty()));
        });

        driver.get("https://unique-hamster-be6d55.netlify.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement buttonElement = driver.findElement(By.cssSelector("button[onclick='makeRequest()']"));

        wait.until(ExpectedConditions.visibilityOf(buttonElement));

        buttonElement.click();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        
        driver.quit();
    }
}
