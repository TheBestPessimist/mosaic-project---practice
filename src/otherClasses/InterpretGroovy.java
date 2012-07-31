package otherClasses;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import allTasksVariablesComputator.AllTasksVariablesComputator;

public class InterpretGroovy {
	
	private static double configuration_file_value = 0;
	
	static Logger log = Logger.getLogger(MakeJson.class.getName());
	
	/**
	 * Interpret the groovy code sent through the parameter
	 * @ BETA!!!!!!!!!!!!!!	<- it has dummy values!
	 * @author Lucian Stefanoaica 
	 * @param condition String containing the groovy code to be evaluated.
	 * @return Value of the evaluated groovy condition.
	 */
	
	public static boolean interpretGroovy(String condition) throws Exception {
		
		Boolean result = null;
		try {
			PopulateBean pb = new PopulateBean();
			
			GenerateAllTasksVariables ATV = new GenerateAllTasksVariables();
			
			AllTasksVariablesComputator ATVC = new AllTasksVariablesComputator(ATV.getAllNodes());
			
			Binding binding = new Binding();
			
			GroovyShell shell = new GroovyShell(binding);
			
			for (String s : pb.getGigaBean().getFrom_machine().getSingle_task_variables()) {
				
				if (s.compareTo("resource_load") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("monetary_cost") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("transport_cost") == 0)
					throw new NotImplementedException();
			}
			for (String s : pb.getGigaBean().getTo_machine().getSingle_task_variables()) {
				
				if (s.compareTo("resource_load") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("monetary_cost") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("transport_cost") == 0)
					throw new NotImplementedException();
			}
			for (String s : pb.getGigaBean().getTo_machine().getAll_tasks_variables()) {
			
				if (s.compareTo("min_resource_load") == 0)
					shell.setVariable("min_resource_load", ATVC.get_min_resource_load(0).getCost());
				
				if (s.compareTo("max_resource_load") == 0)
					shell.setVariable("max_resource_load", ATVC.get_max_resource_load(0).getCost());
				
				if (s.compareTo("min_resource_load_id") == 0)
					shell.setVariable("min_resource_load_id", ATVC.get_min_resource_load(0).getIndex());
				
				if (s.compareTo("max_resource_load_id") == 0)
					shell.setVariable("max_resource_load_id", ATVC.get_max_resource_load(0).getIndex());
				
				if (s.compareTo("max_resource_load_threshold") == 0)
					shell.setVariable("max_resource_load_threshold", configuration_file_value);	// TODO set with a fixed value taken from a configuration file.
				
				if (s.compareTo("min_resource_load_threshold") == 0)
					shell.setVariable("min_resource_load_threshold", configuration_file_value);	// TODO set with a fixed value taken from a configuration file.
				
				if (s.compareTo("min_transport_cost") == 0)
					shell.setVariable("min_transport_cost", ATVC.get_min_transport_cost(ATV.getPartitions()));
				
				if (s.compareTo("max_transport_cost") == 0)
					shell.setVariable("max_transport_cost", ATVC.get_max_transport_cost(ATV.getPartitions()));	
				
				if (s.compareTo("min_transport_cost_resource_id") == 0)
					shell.setVariable("min_transport_cost_resource_id", ATVC.get_min_transport_cost(ATV.getPartitions()).firstElement().getIndex());	//	TODO min_transport_cost_resource_id should be an array.
				
				if (s.compareTo("max_transport_cost_resource_id") == 0)
					shell.setVariable("max_transport_cost_resource_id", ATVC.get_max_transport_cost(ATV.getPartitions()).firstElement().getIndex());	//	TODO max_transport_cost_resource_id should be an array.
				
				if (s.compareTo("min_total_resource_execution_time") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("max_total_resource_execution_time") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("min_total_resource_execution_time_id") == 0)
					throw new NotImplementedException();
				
				if (s.compareTo("max_total_resource_execution_time_id") == 0)
					throw new NotImplementedException();
			}
			result = (Boolean)shell.evaluate(condition);
		}
		catch(Exception e) {
			log.fatal (e + "\nThe program will now exit.");
			System.exit(-1337);
		}
		System.out.println(result);
		return result;
	}
}