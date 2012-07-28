package mosaic.scheduler.platform;

import java.util.Vector;

import org.apache.log4j.Logger;

import mosaic.scheduler.platform.algorithms.IAlgorithm;
import mosaic.scheduler.platform.algorithms.util.AlgorithmUtil;
import mosaic.scheduler.platform.com.json.beans.ComponentLoadListBean;
import mosaic.scheduler.platform.com.json.beans.ComponentWorkflowBean;
import mosaic.scheduler.platform.com.json.beans.NodesBean;
import mosaic.scheduler.platform.resources.Component;
import mosaic.scheduler.platform.resources.ComponentRequirementsList;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.resources.Partition;
import mosaic.scheduler.platform.settings.SystemSettings;
import mosaic.scheduler.simulator.util.Runner;



public class Scheduler {
	private static Logger logger = Logger.getLogger(Runner.class.getPackage().getName());

	Vector<ComponentWorkflowBean> componentWorkflow = null;
	Vector<NodesBean> crtNodes;
	Vector<ComponentLoadListBean> crtComponents;
	Runner.AlgorithmContainer container = null;
	
	public Scheduler(IAlgorithm algorithm) {
		this.container = new Runner(). new AlgorithmContainer(algorithm, new Vector<Node>());
		
		//dummy data - remove in production
		this.putDummyData();
		
		//TODO get component workflow
	}
 	
	public void run() {
		Component component = null;
		Partition partition = null;
		Node node = null;
		
		//do {
		
		//TODO scale components based on traffic at each of the component
		
		this.container.nodes.clear();
		//TODO query node list
		
		for (NodesBean nb : this.crtNodes) {
			//TODO query node for components
			
			//compute the average load of every component type based on input data
			int[] averageComponentLoad = new int[SystemSettings.getSystemSettings().getNo_component_types()];
			int[] noComponentsPerType =  new int[SystemSettings.getSystemSettings().getNo_component_types()];
			for (ComponentLoadListBean c : this.crtComponents) {
				averageComponentLoad[c.getComponent_type()] += Integer.parseInt(c.getComponent_load().split(",")[0]);
				noComponentsPerType[c.getComponent_type()]++;
			}
			
			ComponentRequirementsList crl  = ComponentRequirementsList.getComponentsRequirement();
			for (int i=0; i<averageComponentLoad.length; i++) {
				averageComponentLoad[i] = (noComponentsPerType[i] != 0) ? averageComponentLoad[i]/noComponentsPerType[i] : 0;
				crl.getComponentRequirements(i).setCpuUsage(0, averageComponentLoad[i]);
			}
			
			//add node and components attached to it
			node = new Node(nb.getNode_id(), nb.getNode_datacenter_id(), nb.getNode_cloud_id());	
			for (ComponentLoadListBean ncb : crtComponents) {
				component = new Component(false, ncb.getComponent_type());
				partition = Partition.provide(node.getID());
				partition.addComponent(component);
				node.addPartition(partition);					
			}
			
			this.container.nodes.add(node);
		}

		//add connections to components based on the input data
		for (Component c : AlgorithmUtil.gatherComponents(this.container.nodes)) {		
			for (ComponentWorkflowBean cwb : this.componentWorkflow) {
				if (cwb.getComponent_type() == c.getType()) {
					for (String id : cwb.getLinked_to_Component()) {
						if (id.trim().length() > 0)
							c.addConnection(AlgorithmUtil.getRandomComponent(Integer.parseInt(id), this.container.nodes));									
					}					
				}
			}
		}

		//run the algorithm
		this.container.nodes = this.container.algorithm.executeOnce(this.container.nodes, new int[]{1,7,19},0);/*use this if scaling is enabled*/
		//this.container.nodes = this.container.algorithm.executeOnce(this.container.nodes,0);/*use this if scaling is disabled*/
		
		//compute the number of nodes to be added or removed
		int countAdd = 0, countRemove = 0;
		for (Node n : this.container.nodes) {
			if (n.status == Node.NODE_STATUS.NEW)
				countAdd++;
			if (n.status == Node.NODE_STATUS.TO_BE_REMOVED)
				countRemove++;
		}
			
		Scheduler.logger.debug("Nodes to be added : " + countAdd);
		Scheduler.logger.debug("Nodes to be removed : " + countRemove);
		
		//TODO send message for scaling up/down	and for adding/removing nodes	
		
//		try {
//			Thread.sleep(60000);
//		} catch (InterruptedException e) { }
		//} while (true);
	}

	//TODO unsure whether the following methods are really needed
	
	public synchronized void storeNodes(Vector<NodesBean> nodes) {
		this.crtNodes = nodes;
	}
	
	public synchronized void storeComponentsPerNode(Vector<ComponentLoadListBean> components) {
		this.crtComponents = components;
	}

	public synchronized void storeComponentWorkflow(Vector<ComponentWorkflowBean> workflow) {
		this.componentWorkflow = workflow;
	}
	
	private void putDummyData() {
		//component workflow
		this.componentWorkflow = new Vector<ComponentWorkflowBean>();
		this.componentWorkflow.add(new ComponentWorkflowBean(0, "1", "10", "20"));
		this.componentWorkflow.add(new ComponentWorkflowBean(1, "2", "10", "20"));
		this.componentWorkflow.add(new ComponentWorkflowBean(2, "", "10", "20"));
				
		//node list
		this.crtNodes = new Vector<NodesBean>();
		//add 1st node
		this.crtNodes.add(new NodesBean("1", "1", "1", "45"));
		
		//component list
		this.crtComponents = new Vector<ComponentLoadListBean>();
		//create queue list for 1st component
		//Vector<QueueListBean> queue = new Vector<QueueListBean>();
		//queue.add(new QueueListBean("1", "100"));
		//add 1st component
		this.crtComponents.add(new ComponentLoadListBean(0, "10,12,9"));
		
		//create queue list for 2nd component
		//queue = new Vector<QueueListBean>();
		//queue.add(new QueueListBean("2", "50"));
		//add 2nd component
		this.crtComponents.add(new ComponentLoadListBean(0, "15,13,10"));
		
		//create queue list for 3rd component
		//queue = new Vector<QueueListBean>();
		//queue.add(new QueueListBean("3", "50"));
		//add 3rd component
		this.crtComponents.add(new ComponentLoadListBean(1, "20,13,24"));
		
//		//create queue list for 4th component
//		queue = new Vector<NoMessagesInQueueBean>();
//		queue.add(new NoMessagesInQueueBean("3", "50"));
//		//add 3rd component
//		this.crtComponents.add(new ComponentLoadListBean(2, "20,13,24", queue));

	}
}
