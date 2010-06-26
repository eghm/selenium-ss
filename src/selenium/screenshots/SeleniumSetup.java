// http://binil.wordpress.com/2006/12/22/taking-screenshots-with-selenium/

package selenium.screenshots;

import java.io.File;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import selenium.screenshots.util.Reporter;


public class SeleniumSetup {
    private static Reporter reporter;
    
    @BeforeSuite
    public static void setupReporter() throws Exception {
    	reporter = new Reporter(new File("target" + File.separator + "screenshots"));
    }

    @AfterSuite
    public void cleanupReporter() throws Exception {
    	reporter.close();
    }
    
    public static Reporter getReporter() {
    	if (reporter == null) {
    		try {
				setupReporter();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
    	}
    	return reporter;
    }
}
