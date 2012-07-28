package Beans;
/**
 *
 */

import java.util.Vector;

/**
 * 
 * @author The Best Pessimist
 * @tibi Tibi
 *
 */
public class ToMachineBean {

	private Vector <String> single_task_variables = new Vector <String>();
	private Vector <String> all_tasks_variables = new Vector <String>();
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
	 * @return the all_tasks_variables
	 */
	public Vector<String> getAll_tasks_variables() {
		return all_tasks_variables;
	}
	/**
	 * @param all_tasks_variables the all_tasks_variables to set
	 */
	public void setAll_tasks_variables(Vector<String> all_tasks_variables) {
		this.all_tasks_variables = all_tasks_variables;
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
