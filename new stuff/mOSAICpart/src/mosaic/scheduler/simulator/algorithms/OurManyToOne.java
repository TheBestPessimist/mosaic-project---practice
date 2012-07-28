package mosaic.scheduler.simulator.algorithms;

import java.util.Hashtable;
import java.util.Vector;

import mosaic.scheduler.platform.algorithms.IAlgorithm;
import mosaic.scheduler.platform.algorithms.util.AlgorithmUtil;
import mosaic.scheduler.platform.resources.Component;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.resources.Partition;
import mosaic.scheduler.platform.settings.SystemSettings;
import mosaic.scheduler.test.Test;



public final class OurManyToOne implements IAlgorithm {

	//public static int MAX_ITERATION = 600;
	public static int POPULATION_SIZE = 1;	

	public Vector<Node> init(Vector<Node> nodes, int time) {
		Component component = null;
		for (int i=0; i<SystemSettings.getSystemSettings().getNo_component_types(); i++) {
			component = new Component(false, i);
			//link to other components as indicated by the relationship matrix
			for (int j=0; j<SystemSettings.getSystemSettings().getNo_component_types(); j++) {
				if (SystemSettings.getSystemSettings().getComponent_connection_table()[i][j] == 1) {
					component.addConnection(AlgorithmUtil.getRandomComponent(j, nodes));
				}
			}
			//add component
			AlgorithmUtil.gatherPartitions(nodes).get(this.getPartitionIndex(nodes, component, time)).addComponent(component);
		}		
		return nodes;
	}
	
	@Override
	public Vector<Node> executeOnce(Vector<Node> nodes, int[] componentsToBeGenerated, int time) {
		Component component = null;
			
		for (int i=0; i<componentsToBeGenerated.length; i++) {
			for (int k=0; k<componentsToBeGenerated[i]; k++) {
				component = new Component(false, i);
				//link to other components as indicated by the relationship matrix
				for (int j=0; j<SystemSettings.getSystemSettings().getNo_component_types(); j++) {
					if (SystemSettings.getSystemSettings().getComponent_connection_table()[i][j] == 1) {
						component.addConnection(AlgorithmUtil.getRandomComponent(j, nodes));
					}
				}
				//add component
				AlgorithmUtil.gatherPartitions(nodes).get(this.getPartitionIndex(nodes, component, time)).addComponent(component);
			}
			
		}
		//scale if necessary
		nodes = this.scale(nodes, time);
		String format = "";
		int j=0;
		for (Node n: nodes) {
			format += "|";
			for (int i=0;i<SystemSettings.getSystemSettings().getNo_component_types(); i++)
				format += AlgorithmUtil.getNumberComponents(n, i) + "#";
			// this is where we handle the historical data
			n.history.put(time, n.new NodeHistory(n.computeLoad(time, j++), AlgorithmUtil.getNoComponentTypes(n), AlgorithmUtil.computeNodeCost(n, nodes), format, n.startedSearch, n.isRelayNode, n.isStopped));
		}
		return nodes;
	}
	
	private int getPartitionIndex(Vector<Node> nodes, Component component, int time) {
		Vector<Integer> v = new Vector<Integer>();		
		Vector<Partition> partitions = AlgorithmUtil.gatherPartitions(nodes);

		// select node with least cost
		String nName = "";
		float minCost = Float.MAX_VALUE;
		float cost;
		int k = 0;
		for (Node n : nodes) {
			cost = AlgorithmUtil.computeCostForComponent(nodes, component, n);
			if (cost < minCost && !n.isStopped) {
				minCost = cost + n.computeLoad(time, k++);
				nName = n.getID();
			}
		}

		synchronized (partitions) {
			int pSize = 0;
			for (int i = SystemSettings.getSystemSettings().getMax_number_nodes() * component.getType(); i<(SystemSettings.getSystemSettings().getMax_number_nodes() * component.getType() + SystemSettings.getSystemSettings().getMax_number_nodes() - 1); i++ ) {
				pSize = partitions.get(i).getAssignedComponents().size();
				for (int j=0; j<pSize; j++) {
					v.add(i);
				}
				v.add(i);//1 occurrence for size=0 and +1 for the rest			
			}
		}

		//pick random partition from the partitions assigned to the node that provided the least cost
		//if node does not oversee any partition attached to the component type , pick random 
		int pIndex = 0;
		//int repeats = 0;
		/*Vector<Partition> parts = this.gatherPartitions(nodes);
		do {
			pIndex = v.get(0 + (int)(Math.random() * ((v.size() - 1 - 0) + 1)));
		} while (parts.get(pIndex).getAssignedNodeID().compareToIgnoreCase(nName) != 0 && repeats++ < 100);*/
		
		//pick partition with smallest number of C/Cs
		int smallest = Integer.MAX_VALUE;
		Partition part = null;
		for (Node n: nodes) {
			if (n.getID().compareToIgnoreCase(nName) == 0) {
				for (Partition p : n.getAssignedPartitions()) {
					if (p.getAssignedComponents().size() < smallest) {
						smallest = p.getAssignedComponents().size();
						part = p; 
					}
				}
			}
		}
		
		for (int i=0; i<partitions.size(); i++) {
			if (part != null && partitions.get(i).getID().compareToIgnoreCase(part.getID()) == 0) {
				pIndex = i;
			}
		}
		
		
		return pIndex;
	}
	
	private Vector<Node> scale(Vector<Node> nodes, int time) {		
		// apply GA for each element in population. move random number of partitions from overloaded node to other nodes.
		// after movement pick one in which all nodes are under-loaded & has all component types & cost is minimized
		// if such case is inexistent create node and move random one partition from every component type
		// keep the one which minimizes cost
		Hashtable<Integer,Vector<Node>> mutatedNodes = new Hashtable<Integer, Vector<Node>>();
		for (int i=0; i<OurManyToOne.POPULATION_SIZE; i++) {
			try {
				mutatedNodes.put(i, this.mutate(Test.cloneNodes(nodes), time));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// get the solution with the least cost and that has all nodes under-loaded
		int minIndex = 0;
		float minCost = Float.MAX_VALUE;
		boolean ok = true;
		int k=0;
		for (int i=0; i<OurManyToOne.POPULATION_SIZE; i++) {
			if (AlgorithmUtil.computePlatformCost(mutatedNodes.get(i)) < minCost) {
				ok = true;
				k = 0;
				for (Node n : mutatedNodes.get(i)) {
					if (n.computeLoad(time, k++) > SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
						ok = false;
					}
				}
				if (ok == true) {
					minCost = AlgorithmUtil.computePlatformCost(mutatedNodes.get(i));
					minIndex = i;
				}
			}
		}

		return mutatedNodes.get(minIndex);
	}
	
	private Vector<Node> mutate(Vector<Node> nodes, int time) {
		Partition partition = null;
		int repeats = 0, repeats2 = 0;
		int maxRepeats = 0, index = 0;
		Node n2 = null, newNode = null;
		Vector<Partition> parts = null;
		Node n;
		int m;
		for (int k=0; k<nodes.size(); k++) {
			n = nodes.get(k);
			repeats = 0;
			maxRepeats = 30;
			while (n.computeLoad(time, k) > SystemSettings.getSystemSettings().getMax_node_load_threshold() && maxRepeats > repeats++) {
				repeats2 = 0;
				//pick random node which is under-loaded
				do {		
					 m = 0 + (int)(Math.random() * ((nodes.size() - 1 - 0) + 1));
			
					if (repeats2 < maxRepeats) {
						n2 = nodes.get(m);
						//pick random partition
						partition = n.getAssignedPartitions().get(0 + (int)(Math.random() * ((n.getAssignedPartitions().size() - 1 - 0) + 1)));
						if (n2.getID().compareToIgnoreCase(n.getID()) != 0)
						if (n2.computeLoad(time, m) + partition.computeLoad(time) < SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
							n.getAssignedPartitions().remove(partition);
							//reassign it
							n2.addPartition(partition);
							partition.assignToNode(n2.getID());
						}
					}
				} while (maxRepeats > repeats2++);
				
				// we were not able to decrease load so create new node
				if (repeats2 >= maxRepeats) {
					if (nodes.size() < SystemSettings.getSystemSettings().getMax_number_nodes() + AlgorithmUtil.computeNoFailedNodes(nodes)) {
						newNode = AlgorithmUtil.addNewRandomNode();
						nodes.add(newNode);
						
						//decrease load of node by moving random partitions
						while (n.computeLoad(time, k) > SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
							//pick random a partition from overloaded node
							partition = n.getAssignedPartitions().remove(0 + (int)(Math.random() * ((n.getAssignedPartitions().size() - 1 - 0) + 1)));
							//reassign it
							newNode.addPartition(partition);
							partition.assignToNode(newNode.getID());
						}						
						//reassign a partition of each type to new node
						
						parts = AlgorithmUtil.gatherPartitions(nodes);
						Vector<Partition> aParts;
						Node tmpNode;
						for (int j=0; j<SystemSettings.getSystemSettings().getNo_component_types(); j++) {
							int reps = 0;
							do {
								index = SystemSettings.getSystemSettings().getMax_number_nodes() * j + (int)(Math.random() * ((SystemSettings.getSystemSettings().getMax_number_nodes() * j + SystemSettings.getSystemSettings().getMax_number_nodes() - 1 - SystemSettings.getSystemSettings().getMax_number_nodes() * j) + 1));
							} while (parts.get(index).getAssignedComponents().size() == 0 && reps++ < maxRepeats);
						
							partition = parts.get(index);
							tmpNode = null;
							for (Node node : nodes) {								
								if (node.getID().compareToIgnoreCase(parts.get(index).getAssignedNodeID()) == 0) {
									aParts = node.getAssignedPartitions();
									for (int i=0; i<aParts.size(); i++) {
										if (aParts.get(i).getID().compareToIgnoreCase(partition.getID()) == 0)
											partition = aParts.get(i);
											tmpNode = node;
											//found = true;
											//break;
										
									}
								}
								//if (found)
								//	break;
							}
							if (newNode.computeLoad(time, nodes.size()-1) + partition.computeLoad(time) < SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
								tmpNode.removePartition(partition);
								//reassign it
								newNode.addPartition(partition);								
								partition.assignToNode(newNode.getID());
							}
						}
					}
				}
			}
		}
		return nodes;
	}

	@Override
	public Vector<Node> executeOnce(Vector<Node> nodes, int time) {
		throw new UnsupportedOperationException();
	}
}
