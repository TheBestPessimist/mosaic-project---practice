package junitTestClasses;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * This class can be used to basically run all junit test classes inside the Eclipse IDE
 * right click on it and run as JUnit test (in the package manager)
 * other junit test classes can be added to the test suit by simply putting: ClassName.class in the suite bellow
 * @author Lucian Stefanoaica
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InterpretGroovyTest.class, MakeJsonTest.class, PopulateBeanTest.class })
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for default package");
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}
}