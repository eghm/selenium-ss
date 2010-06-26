// http://binil.wordpress.com/2006/12/22/taking-screenshots-with-selenium/

package selenium.screenshots.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Reporter {
    private int testCount = 1;
    
    private File reportsDir;

    private Rectangle screenBounds;
    private Robot robot;
    private Configuration cfg;
    private SimpleDateFormat formatter;

    private File currentTestDir;
    private int imageCount = 1;
    private Map testModel;
    
    public static final String SUMMARY_TEMPLATE = "summary.ftl";
    public static final String TEST_TEMPLATE = "test.ftl";
    public static final String SHADOW_IMG = "shadow.gif";
    public static final String SHADOW_ALPHA_IMG = "shadowAlpha.png";
    
    public Reporter(File reportsDir) throws AWTException, IOException {
        this.reportsDir = reportsDir;
        this.reportsDir.mkdirs();
        
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        screenBounds = new Rectangle(0, 0, screenDim.width, screenDim.height);
        robot = new Robot();
        
        File templatesDir = templateReportsDir(reportsDir);
        
        cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templatesDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        
        formatter = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss z");
    }

	private File templateReportsDir(File reportsDir) throws IOException {
		File templatesDir = new File(reportsDir, "templates");
        templatesDir.mkdirs();
        if (System.getProperty("sess.template.skip") == null) {        
	        copy(templatesDir, SUMMARY_TEMPLATE);
	        copy(templatesDir, TEST_TEMPLATE);
	        copy(templatesDir, SHADOW_ALPHA_IMG);
	        copy(templatesDir, SHADOW_IMG);
        }
		return templatesDir;
	}

	private void copy(File templatesDir, String template) throws IOException {
		URL source = this.getClass().getClassLoader().getResource(template);
		if (source == null) {
			throw new RuntimeException("Unable to find " + template + " to copy to " + templatesDir.getAbsolutePath() + " (ClassLoader issue?) set Java Property sess.template.skip=true after copying files manually to contine");
		}
		FileUtils.copyURLToFile(source , new File(templatesDir, template));
	}
    
    void startTest() {
        imageCount = 1;
        currentTestDir = new File(reportsDir, "test" + (testCount++));
        currentTestDir.mkdirs();
        
        testModel = new HashMap();
        testModel.put("name", (testCount - 1));
        testModel.put("screenshots", new SimpleSequence());        
        testModel.put("start", formatter.format(new Date()));
    }

    void endTest() { 
        File summary = new File(currentTestDir, "index.html");
        Writer out = null;
        try {
            Template temp = cfg.getTemplate(TEST_TEMPLATE);        
            out = new FileWriter(summary);
            temp.process(testModel, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally { 
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    void recordStep(String description) { 
        File screenshot = new File(currentTestDir, "image" + (imageCount++) + ".png");
        BufferedImage image =  robot.createScreenCapture(screenBounds);
        try {
            ImageIO.write(image, "png", screenshot);
        } catch (IOException ignore) {
            ignore.printStackTrace();
        } 
        
        Map<String, String> thisScreenShot = new HashMap<String, String>();
        thisScreenShot.put("image", "image" + (imageCount-1) + ".png");
        thisScreenShot.put("description", description);
        ((SimpleSequence) testModel.get("screenshots")).add(thisScreenShot);
    }

    public void close() {
        Map model = new HashMap();
        model.put("tests", testCount - 1);
		
        File summary = new File(reportsDir, "index.html");
        Writer out = null;
        try {
            Template temp = cfg.getTemplate(SUMMARY_TEMPLATE);        
            out = new FileWriter(summary);
            temp.process(model, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally { 
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
