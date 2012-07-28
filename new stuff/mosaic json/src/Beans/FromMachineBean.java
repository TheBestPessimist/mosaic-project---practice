package Beans;

import java.util.Vector;

/**
 * 
 * @author The Best Pessimist
 *
 */
public class FromMachineBean
{
	private Vector <String> single_task_variables = new Vector <String>();
	private String condition = "";
	
	/**
	 * @return the single_task_variables
	 */
	public Vector<String> getSingle_task_variables() {
		return single_task_variables;
	}
	/**
	 * @param single_task_variables the single_task_variables to set
	 */
	public void setSingle_task_variables(Vector<String> single_task_variables) {
		this.single_task_variables = single_task_variables;
	}
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

}
