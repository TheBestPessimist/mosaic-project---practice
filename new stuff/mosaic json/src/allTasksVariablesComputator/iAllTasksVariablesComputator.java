package allTasksVariablesComputator;

import java.util.Vector;

import allTasksVariablesComputator.AllTasksVariablesComputator.MinMaxResource;

import mosaic.scheduler.platform.resources.Partition;


/**
 * The interface implementing All Tasks Variables Computator.
 * @author The Best Pessimist
 */
public interface iAllTasksVariablesComputator {

	/*
	 * A bean named MinMaxResource is needed in order to return the data from the functions below.
	 * It should contain a variable which stores the cost\load, and a string which stores the ID, along with it's getters and setters.
	 */

	/**
	 * Return the node with the minimum resource load and its ID in a MinMaxResource bean. 
	 * @param time the time when the load is calculated
	 * @return the bean
	 */
	public MinMaxResource get_min_resource_load(int time);
	
	
	/**
	 * Return the node with the maximum resource load and its ID in a MinMaxResource bean.
	 * @param time the time when the load is calculated
	 * @return the bean
	 */
	public MinMaxResource get_max_resource_load(int time);
	
	
	/**
	 * Return minimum transportation cost for moving a partition to another node and the ID of the node.  
	 * @param partition Partition to be moved
	 * @return the cost and the ID in a MinMaxResource bean
	 */
	public MinMaxResource get_min_transport_cost (Partition partition);
	
	
	/**
	 * Returns a Vector of minimum costs  for the vector of partitions received as parameter.
	 * @param partitions Vector of partitions
	 * @return vector containing the costs and IDs in MinMaxTransportCost beans, respectively
	 */
	public Vector <MinMaxResource>  get_min_transport_cost (Vector <Partition> partitions);


	/**
	 * Returns maximum transport cost for moving a partition to another node, and the ID of the node.
	 * @param partition the partition to be moved
	 * @return a MinMaxResource bean containing the node ID and the cost for relocation
	 */
	public MinMaxResource get_max_transportation_cost (Partition partition);
	
	
	/**
	 * Returns a vector of maximum costs for the vector of partitions given as argument. 
	 * @param partitionsVector argument for the function.
	 * @return vector of maximum costs
	 */
	public Vector <MinMaxResource> get_max_transport_cost (Vector <Partition> partitionsVector);
	
//  	####################################################	
	
	/**
	 * Can not be computed with the methods currently available. 
	 * @throws Exception 
	 */
	public void get_min_total_resource_execution_time() throws Exception;
	
	/**
	 * Can not be computed with the methods currently available. 
	 * @throws Exception 
	 */
	public void get_max_total_resource_execution_time() throws Exception;
	
//	###################################################
}