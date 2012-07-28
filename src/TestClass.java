import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import otherClasses.GenerateAllTasksVariables;
import otherClasses.MakeJson;
import otherClasses.PopulateBean;

/**
 * Class used for testing all the stuff written.
 * @author The Best Pessimist
 *
 */
public class TestClass {
	
	static Logger log = Logger.getLogger (MakeJson.class.getName());
//	 this has to be put in every class where i want to use the logger

	
	public static void main(String args[]) throws Exception {
		PropertyConfigurator.configure("logging.properties");
		log.info("\nTesting started...");
//		 this has to be put only one in the main function, so that logger knows
//																		 its configuration

		PopulateBean pb = new PopulateBean();
		GenerateAllTasksVariables  ATV = new GenerateAllTasksVariables();
		
		
		
		
		/*
		InterpretGroovy ig = new InterpretGroovy();
//		this is a string, so i can cancatenate other stuff as a parameter
//		ig.interpretGroovy ("resource_load=677 \n  monetary_cost=7 \n" + pb.getGigaBean().getFrom_machine().getCondition());
//		groovy interpreter is working!
		
		JSONObject jObj =  MakeJson.makeJson(pb.getGigaBean());
//		System.out.println(jObj);
//		creating json objects from bean works!

*/

		
		
		log.info("Exiting successfully the program...\n\n");
	}

}
