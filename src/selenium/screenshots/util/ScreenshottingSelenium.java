// http://binil.wordpress.com/2006/12/22/taking-screenshots-with-selenium/

package selenium.screenshots.util;

import selenium.screenshots.SeleniumSetup;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

public class ScreenshottingSelenium extends DefaultSelenium {
    private Reporter reporter;
    
    public ScreenshottingSelenium(CommandProcessor processor, Reporter reporter) {
        super(processor);
        this.reporter = reporter;
    }

    public ScreenshottingSelenium(String serverHost, int serverPort,
            String browserStartCommand, String browserURL, Reporter reporter) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
        this.reporter = reporter;
    }

    @Override
    public void start() {
    	if (reporter == null) {
    		reporter = SeleniumSetup.getReporter();
    	}
        reporter.startTest();
        super.start();
        windowMaximize();
    }

    @Override
    public void stop() {
        super.stop();
        if (reporter != null) {
        	reporter.endTest();
        }
    }

    @Override
    public void click(String locator) {
        windowFocus();
        super.click(locator);
        reporter.recordStep("Clicked");
    }

    @Override
    public void open(String url) {
        windowFocus();
        super.open(url);
        reporter.recordStep("Opened " + url);
    }

    @Override
    public void type(String locator, String value) {
        windowFocus();
        super.type(locator, value);
        reporter.recordStep("Typed '" + value + "'");
    }

    @Override
    public void waitForPageToLoad(String timeout) {
//        windowFocus(); // this seems to cause timeouts (also doesn't work)?
        super.waitForPageToLoad(timeout);
        reporter.recordStep("Page loaded");
    }
}
