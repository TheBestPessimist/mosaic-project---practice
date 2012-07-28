package otherClasses;

import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;

import mosaic.scheduler.platform.resources.Component;
import mosaic.scheduler.platform.resources.ComponentRequirementsList;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.resources.Partition;
import mosaic.scheduler.platform.settings.SystemSettings;
import mosaic.scheduler.simulator.resources.ComponentRequirements;

public class GenerateAllTasksVariables {


	private Vector <Partition> partitions = new Vector <Partition>();
	private Vector <Node> allNodes = new Vector<Node>();	
	
	/**
	 * The usual logger.
	 */
	static Logger log2 = Logger.getLogger(GenerateAllTasksVariables.class);
	
	
	/**
	 * Class which generates stuff. (nothing to do here)
	 * @throws Exception 
	 */
	public GenerateAllTasksVariables() throws Exception
	{
		
		/*
		 * Below, i loaded a config file for SystemSettings. This had to be done in order to use marc's provided code.
		 * (code borrowed from marc)
		 */
		SystemSettings.getSystemSettings().loadProperties("mosaic/scheduler/simulator/settings/system.properties");
		for (int i=0; i < SystemSettings.getSystemSettings().getTime_span(); i++) {
			ComponentRequirementsList crl  = ComponentRequirementsList.getComponentsRequirement();
			crl.addComponentRequirement(i, new ComponentRequirements());
		}
		
		
		Node aNode = new Node (UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Node cNode = new Node (UUID.randomUUID().toString(), UUID.randomUUID().toString());

		Partition aPartition = Partition.provide (aNode.getID());
		Partition bPartition = Partition.provide (cNode.getID());
		Partition cPartition = Partition.provide (cNode.getID());
		Partition dPartition = Partition.provide (aNode.getID());
		
		Component aComponent = new Component(false, 0);		
		Component bComponent = new Component( true , 1);
		Component cComponent = new Component( true , 2);
		Component dComponent = new Component( true , 3);
		Component eComponent = new Component(false, 0);		
		Component fComponent = new Component( true , 1);
		Component gComponent = new Component( false , 4);
		Component hComponent = new Component( true , 3);
		
		
//	#######################################################
		
		
		aPartition.addComponent (aComponent);
		aPartition.addComponent(bComponent);
		bPartition.addComponent(cComponent);
		cPartition.addComponent(dComponent);
		cPartition.addComponent(hComponent);
		dPartition.addComponent(hComponent);
		cPartition.addComponent(eComponent);
		dPartition.addComponent(bComponent);
		bPartition.addComponent(hComponent);
		cPartition.addComponent(eComponent);
		bPartition.addComponent(cComponent);
		dPartition.addComponent(fComponent);
		cPartition.addComponent(gComponent);
		aPartition.addComponent(hComponent);

//		######################################################
		
		aComponent.addConnection ("connected with tibi");
		aComponent.addConnection ("connected with gogu");
		bComponent.addConnection (aComponent.getID());
		bComponent.addConnection (cComponent.getID());
		
//	#######################################################
		
		aNode.addPartition(aPartition);
		aPartition.assignToNode(aNode.getID());
		aNode.addPartition(cPartition);
		cPartition.assignToNode(aNode.getID());
		cNode.addPartition(dPartition);
		dPartition.assignToNode(cNode.getID());
		cNode.addPartition(bPartition);
		bPartition.assignToNode(cNode.getID());
		
//		#######################################################

//		for (int i = 0; i < SystemSettings.getSystemSettings().getTime_span(); ++i)
//			System.out.println ("compute load: " + i + " " +  aPartition.computeLoad(i));
		
//		##################################################################
		
		partitions.add(cPartition);
		partitions.add(bPartition);
		
//		################################################################

		allNodes.add(cNode);
		allNodes.add(aNode);

		
//		#########################################################

/*
		AllTasksVariablesComputator computeiT = new  AllTasksVariablesComputator(allNodes);
		MinMaxResource mmtc = computeiT.get_min_transport_cost (cPartition);
//		System.out.println ("\n\n" + mmtc.getCost() + "\n" + mmtc.getId());
		
		Vector <MinMaxResource> vmmtc = new Vector <MinMaxResource>();
		vmmtc = computeiT.get_min_transport_cost (partitions);

	*/
	}


	/**
	 * @return the partitions
	 */
	public Vector<Partition> getPartitions() {
		return partitions;
	}


	/**
	 * @param partitions the partitions to set
	 */
	public void setPartitions(Vector<Partition> partitions) {
		this.partitions = partitions;
	}


	/**
	 * @return the allNodes
	 */
	public Vector<Node> getAllNodes() {
		return allNodes;
	}


	/**
	 * @param allNodes the allNodes to set
	 */
	public void setAllNodes(Vector<Node> allNodes) {
		this.allNodes = allNodes;
	}
	
	
	
	



}
