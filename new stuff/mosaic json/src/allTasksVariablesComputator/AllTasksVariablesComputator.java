package allTasksVariablesComputator;

import java.util.Vector;

import mosaic.scheduler.platform.algorithms.util.AlgorithmUtil;
import mosaic.scheduler.platform.algorithms.util.AlgorithmUtil.CostData;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.resources.Partition;


/**
 * Class for calculating some all_tasks_variables variables,
 * that will be used in the groovy condition execution. 
 * @author The Best Pessimist
 */
public class AllTasksVariablesComputator implements iAllTasksVariablesComputator{
	
	/**
	 * Stores the nodes where the search will be done.
	 */
	private Vector <Node> allNodes;
	
	
	/**
	 * Private constructor.
	 */
	private AllTasksVariablesComputator() {}

	
	/**
	 * Constructor.
	 * @param allNodes Vector containing all the nodes
	 */
	public AllTasksVariablesComputator( Vector <Node> allNodes)
	{
		this.allNodes = allNodes;
	}
	

	/**
	 * Check interface for info.
	 */
	public MinMaxResource get_min_resource_load(int time)
	{
		MinMaxResource resource = new MinMaxResource();
		int i = 0;
		int nodeLoad = 0;
		for(i = 0; i < allNodes.size() - 1; ++i)
		{
			nodeLoad = allNodes.elementAt(i).computeLoad(time, i);
			if( nodeLoad < resource.getCost() )
			{
				resource.setCost(nodeLoad);
				resource.setIndex(allNodes.elementAt(i).getID());
			}
		}
		
		return resource;
	}
	
	/**
	 * Check interface for info.
	 */
	public MinMaxResource get_max_resource_load(int time)
	{

		MinMaxResource resource = new MinMaxResource();
		int i = 0;
		int nodeLoad = 0;
		for(i = 0; i < allNodes.size() - 1; ++i)
		{
			nodeLoad = allNodes.elementAt(i).computeLoad(time, i);
			if( nodeLoad > resource.getCost() )
			{
				resource.setCost(nodeLoad);
				resource.setIndex(allNodes.elementAt(i).getID());
			}
		}
		
		return resource;
	}

	
	/**
	 * Check interface for info.
	 */
	public MinMaxResource get_min_transport_cost (Partition partition)
	{
		MinMaxResource mmtc =  new AllTasksVariablesComputator(). new MinMaxResource();
		mmtc.setCost(0);
		Vector <CostData> relocationCosts = new Vector <CostData>();
		relocationCosts = AlgorithmUtil.computePartitionRelocationCost (partition, allNodes);
		mmtc.setIndex (relocationCosts.firstElement().getNode().getID());
		mmtc.setCost(relocationCosts.firstElement().getCost());
		return mmtc;
	}
	
	
	/**
	 * Check interface for info.
	 */
	public Vector <MinMaxResource> get_min_transport_cost (Vector <Partition> partitionsVector)
	{
		Vector <MinMaxResource> moveCosts = new Vector < MinMaxResource>(); 
		MinMaxResource aux = new MinMaxResource();
		for (Partition partition: partitionsVector)
		{
			aux = get_min_transport_cost (partition);
			moveCosts.add(aux);
		}
		
		return moveCosts;
	}


	/**
	 * Check interface for info.
	 */
	public MinMaxResource get_max_transportation_cost (Partition partition)
	{
		MinMaxResource mmtc = new MinMaxResource();
		Vector <CostData> cost = new Vector <CostData> ();
		cost = AlgorithmUtil.computePartitionRelocationCost(partition, allNodes);
		mmtc.setCost(cost.lastElement().getCost());
		mmtc.setIndex(cost.lastElement().getNode().getID());
		return mmtc;
	}
	
	
	/**
	 * Check interface for info.
	 */
	public Vector <MinMaxResource> get_max_transport_cost (Vector <Partition> partitionsVector)
	{
		Vector <MinMaxResource> vmmtc = new Vector <MinMaxResource>();
		MinMaxResource aux = new MinMaxResource();
		for (Partition partition: partitionsVector)
		{
			aux = get_max_transportation_cost(partition);
			vmmtc.add(aux);		
		}
		return vmmtc;
	}


	/**
	* Check interface for info.
	 * Can not be computed with the methods currently available. 
	 * @throws Exception 
	 */
	public void get_min_total_resource_execution_time() throws Exception
	{
		throw new Exception("I can not be computed with the methods currently available. Fix me! (oh, my ID too!)");
//		nothing to do here...
	}

	
	/**
	 * Check interface for info.
	 * Can not be computed with the methods currently available. 
	 * @throws Exception 
	 */
	public void get_max_total_resource_execution_time() throws Exception
	{
		throw new Exception("I can not be computed with the methods currently available. Fix me! (oh, my ID too!)");
//		nothing to do here...
	}
	
	
	/**
	 * Class used as return object for the computation
	 * of min/max transport cost node and it's ID.
	 * @author The Best Pesimist
	 *
	 */
	public class MinMaxResource
	{
		/*
		 * Stores the cost resulted from the computation.
		 */
		private double cost = 0;
		
		/*
		 * Stores the ID of the node resulted from the computation.
		 */
		private String index = "";

		/**
		 * @return the cost
		 */
		public double getCost() {
			return cost;
		}

		/**
		 * @param cost the cost to set
		 */
		public void setCost(double cost) {
			this.cost = cost;
		}

		/**
		 * @return the id
		 */
		public String getIndex() {
			return index;
		}

		/**
		 * @param id the id to set
		 */
		public void setIndex(String id) {
			this.index = id;
		}
	}
	
	}