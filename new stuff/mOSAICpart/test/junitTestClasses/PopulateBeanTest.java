package junitTestClasses;
import otherClasses.*;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
/**
 * This class can be used to test the following:
 * 1) missing condition either from From_machine or from To_machine
 * 2) The Single_task_variables vector either from From_machine or from To_machine is empty
 * 3) The All_task_variables vector either from From_machine or from To_machine is empty
 * just after a bean is populated
 * @author Lucian Stefanoaica
 *
 */
public class PopulateBeanTest {
	static Logger log = Logger.getLogger(InterpretGroovy.class.getName());
	@Test
	public void testPopulateBean() {
		PropertyConfigurator.configure("logging.properties");
		PopulateBean ABean = new PopulateBean();
		
		assertTrue("The condition string found in From_machiine is empty.",!ABean.getGigaBean().getFrom_machine().getCondition().isEmpty());
		
		assertTrue("The condition string found in To_machine is empty.", !ABean.getGigaBean().getTo_machine().getCondition().isEmpty());
		
		assertTrue("The Single_task_variables vector found in From_machine is empty.", !(ABean.getGigaBean().getFrom_machine().getSingle_task_variables().size() == 0));
		
		assertTrue("The Single_task_variables vector found in To_machine is empty.", !(ABean.getGigaBean().getTo_machine().getSingle_task_variables().size() == 0));
		
		assertTrue("The All_tasks_variables vector found in To_machine is empty.", !(ABean.getGigaBean().getTo_machine().getAll_tasks_variables().size() == 0));

//		assertTrue("From_machine object is missing", !(ABean.getGigaBean().getFrom_machine() == null));
		
//		assertTrue("To_machine object is missing.", !(ABean.getGigaBean().getTo_machine() == null));		
		
//		The last two assertions are no longer needed since the program exits whenever one of the two are null.
	}

}