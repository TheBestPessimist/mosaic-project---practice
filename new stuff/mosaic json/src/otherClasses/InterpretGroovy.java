package otherClasses;
import org.apache.log4j.Logger;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;


public class InterpretGroovy {
	
	static Logger log = Logger.getLogger(MakeJson.class.getName());
	
	
	
	@Deprecated
	/**
	 * Interpret the groovy code sent through the parameter
	 * @ BETA!!!!!!!!!!!!!!	<- it has dummy values!
	 * @author Lucian Stefanoaica 
	 * @param condition String containing the groovy code to be executed.
	 * @return Value of groovy condition.
	 * @Deprecated
	 */
	public static boolean interpretGroovy(String condition) {
		
		Boolean result = null;
		try {
			Binding binding = new Binding();
			GroovyShell shell = new GroovyShell(binding);
			shell.setVariable("resource_load", 90.00000000000001);
			shell.setVariable("monetary_cost", 0.9);
			result = (Boolean)shell.evaluate(condition);
		}
		catch(Exception e) {
			log.fatal (e + "\nThe program will now exit.");
			System.exit(-1337);
		}		
		return result;
	}
}
