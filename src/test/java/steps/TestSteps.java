package steps;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class TestSteps {
    //    private RemoteWebDriver driver;
    private static final ThreadLocal<WebDriver> instance = new InheritableThreadLocal<WebDriver>();

    private WebDriver getDrivet() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities().chrome();
        capabilities.setVersion("74.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("screenResolution", "1024x768x24");
        capabilities.setCapability("enableVideo", true);

        return new RemoteWebDriver(
                new URL("http://10.23.7.67:4444/wd/hub"), capabilities);

    }

    @Before
    public void before() throws MalformedURLException {
        instance.set(getDrivet());
    }

    @After
    public void after(Scenario scenario) throws MalformedURLException {
        if (scenario.isFailed()) {
            scenario.write("Scenario failed so capturing a screenshot");

            TakesScreenshot screenshot = (TakesScreenshot) instance.get();
            scenario.embed(screenshot.getScreenshotAs(OutputType.BYTES), "image/png");
        }
        if (instance.get() != null) {
            instance.get()
                    .quit();
        }
    }

    @Given("^I navigate to Stack Overflow$")
    public void iNavigateToStackOverflow() throws MalformedURLException {
        instance.get()
                .navigate()
                .to("https://stackoverflow.com/");
    }

    @When("I navigate to Stack Overflow question page (.*)")
    public void navigateToStackOverflowQuestionPage(Integer page) throws MalformedURLException {
        instance.get()
                .navigate()
                .to("https://stackoverflow.com/questions?page=" + page);
    }

    @Then("I verify Stack Overflow question page (.*) is opened")
    public void verifyCorrectQuestionPageIsOpened(Integer page) throws MalformedURLException {
        if (!instance.get()
                     .getTitle()
                     .contains("Page " + page)) {
            throw new RuntimeException("The Stack Overflow page title does not contain the page number: " + page);
        }
    }

    @When("I use the following data table to navigate to a Stack Overflow question page")
    public void useTheFollowingDataTable(DataTable dataTable) throws MalformedURLException {
        int pageNumber = Integer.valueOf(dataTable.cell(1, 0));
        navigateToStackOverflowQuestionPage(pageNumber);
    }
}
