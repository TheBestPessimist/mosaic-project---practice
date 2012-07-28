package junitTestClasses;
import otherClasses.*;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
/**
 * This class is used to test if the groovy condition sent as parameter to the interpretGroovy function is valid or not.
 * @author Lucian Stefanoaica
 * 
 */
public class InterpretGroovyTest {
	static Logger log = Logger.getLogger(InterpretGroovy.class.getName());
	@Test
	public void testInterpretGroovy() {
		PropertyConfigurator.configure("logging.properties");
		PopulateBean ABean = new PopulateBean();
		try {
			assertFalse("The assertion is unfulfiled", !InterpretGroovy.interpretGroovy(ABean.getGigaBean().getFrom_machine().getCondition()));
		}
		catch (Exception e) {
			log.error("The groovy condition failed to be evaluated because of:" + e.getMessage());
			e.printStackTrace();
		}
	}
}