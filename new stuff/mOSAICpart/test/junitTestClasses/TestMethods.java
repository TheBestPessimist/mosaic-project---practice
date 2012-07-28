package junitTestClasses;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
/**
 * This class is used to perform all jUnit test classes through code
 * in order to add more jUnit test classes just add another Result just like bellow and iterate through the failures
 * @author Lucian Stefanoaica
 *
 */
public class TestMethods {
	public static void startTesting() {
		Result result1 = JUnitCore.runClasses(InterpretGroovyTest.class);
		for (Failure failure : result1.getFailures()) {
			System.out.println(failure.toString());
		}	
		Result result2 = JUnitCore.runClasses(MakeJsonTest.class);
		for (Failure failure : result2.getFailures()) {
			System.out.println(failure.toString());
		}
		Result result3 = JUnitCore.runClasses(PopulateBeanTest.class);
		for (Failure failure : result3.getFailures()) {
			System.out.println(failure.toString());
		}
	}
}
