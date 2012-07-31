package mosaic.scheduler.simulator.algorithms;

import java.util.Vector;

import mosaic.scheduler.platform.algorithms.IAlgorithm;
import mosaic.scheduler.platform.algorithms.util.AlgorithmUtil;
import mosaic.scheduler.platform.resources.Component;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.resources.Partition;
import mosaic.scheduler.platform.settings.SystemSettings;
import mosaic.scheduler.simulator.util.Runner;

import org.apache.log4j.Logger;


import simgrid.topology.generator.BarabasiAlbert;
import simgrid.topology.graph.WeightedGraph;

public class DistributedOur implements IAlgorithm {
	private static Logger logger = Logger.getLogger(Runner.class.getPackage().getName());

	WeightedGraph graph = null;
	
	private static int NO_PRIMARY_NODES = 2;
	
	Vector<Node> nodes = null, primaryNodes = null;
	
	public DistributedOur () throws Exception {
		this.graph = new BarabasiAlbert(SystemSettings.getSystemSettings().getMax_number_nodes())
						.generate(1);
		//this.graph.print();
	}
	
	@Override
	public Vector<Node> executeOnce(Vector<Node> nodes, int time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Node> executeOnce(Vector<Node> nodes,
			int[] componentsToBeGenerated, int time) {		
		
		this.nodes = nodes;
		this.primaryNodes = this.selectPrimarySpawningNodes();
		this.assignComponents(componentsToBeGenerated);

		int j = 0;
		for (int i=0; i<nodes.size(); i++) {
					if (nodes.get(i).computeLoad(time, i) < SystemSettings.getSystemSettings().getMin_node_load_threshold() 
						&& !nodes.get(i).startedSearch) {
						nodes.get(i).startedSearch = true;
						DistributedOur.logger.info("Underloaded node " + i + " : " + nodes.get(i).computeLoad(time, i));
						//Method 2: if average neighbor load < getMin_node_load_threshold remove node directly
						double avgNeighborLoad = 0;
						int sumNeighbors = 0;
						for (Integer neighbor : this.graph.neighbors(i)) {
							if (neighbor >= this.nodes.size())
								continue;
							avgNeighborLoad += nodes.get(neighbor).computeLoad(time, neighbor);
							sumNeighbors++;
						}
						avgNeighborLoad /= sumNeighbors;
						//if (avgNeighborLoad < SystemSettings.getSystemSettings().getMin_node_load_threshold()) {
						if (nodes.get(i).computeLoad(time, i) < SystemSettings.getSystemSettings().getMin_node_load_threshold()) {
							int ii = 0;
							while (ii < nodes.get(i).getAssignedPartitions().size()) {
								Partition p = nodes.get(i).getAssignedPartitions().get(ii);
								nodes.get(i).removePartition(p);
								ii++;
							}
							DistributedOur.logger.info("Removed underloaded node " + i + " : " + nodes.get(i).computeLoad(time, i));
							nodes.get(i).startedSearch = false;
						}
						/*
						else {
							//attempt to relieve node to neighbors
							DistributedOur.logger.debug("Node is not isolated (average neighbor load < getMin_node_load_threshold : " + avgNeighborLoad);
							DistributedOur.logger.debug("Trying to relocate (under)load towards neighbors");
							for (Integer neighbor : this.graph.neighbors(i)) {
								if (neighbor >= this.nodes.size())
									continue;
								this.attemptRelocation(i, neighbor, time, true);
							}
							//we failed to eliminate load from under loaded node
							if (nodes.get(i).computeLoad(time, i) > 0) {
								DistributedOur.logger.debug("Failed to relocate entire (under)load toward neighbors");
								nodes.get(i).startedSearch = true;
								// pick a random neighbor
								j = this.pickNeighbor(nodes, i);
								nodes.get(j).setVisited(i);
								nodes.get(j).isRelayNode = true;
								DistributedOur.logger.debug("Selected relay node " + j);
								this.attemptRelocation(i, j, time, false);
								if (nodes.get(j).isRelayNode) {
									//and remove its print from the neighbor
									nodes.get(j).removeVisited(i);
									nodes.get(j).isRelayNode = false;
								}		
							}
							if (nodes.get(i).computeLoad(time, i) <= 0) {
								DistributedOur.logger.debug("Releaved node (directly) " + i +" -> " + j);
								nodes.get(i).startedSearch = false;														
							}							
						}*/
					}
					if (nodes.get(i).computeLoad(time, i) > SystemSettings.getSystemSettings().getMax_node_load_threshold()
						&& !nodes.get(i).startedSearch){
						j = 0;
						DistributedOur.logger.debug("Overloaded node " + i + " : " + nodes.get(i).computeLoad(time, i));
						DistributedOur.logger.debug("Trying to relocate load towards neighbors");
						for (Integer neighbor : this.graph.neighbors(i)) {
							if (neighbor >= this.nodes.size())
								continue;
							this.attemptRelocation(i, neighbor, time, false);
						}
						
						if (nodes.get(i).computeLoad(time, i) > SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
							DistributedOur.logger.debug("Failed to relocate entire load toward neighbors");
							nodes.get(i).startedSearch = true;
							// pick a random neighbor
							j = this.pickNeighbor(nodes, i);
							nodes.get(j).setVisited(i);
							nodes.get(j).isRelayNode = true;
							DistributedOur.logger.debug("Selected relay node " + j);
							this.attemptRelocation(i, j, time, false);
						}
										
						//if we managed to eliminate its overload in one shot
						if (nodes.get(i).computeLoad(time, i) <= SystemSettings.getSystemSettings().getMax_node_load_threshold()) {	
							//restore its status
							nodes.get(i).startedSearch = false;						
							if (nodes.get(j).isRelayNode) {
								//and remove its print from the neighbor
								nodes.get(j).removeVisited(i);
								nodes.get(j).isRelayNode = false;
							}		
							DistributedOur.logger.debug("Releaved node (directly) " + i +" -> " + j);
						}
					} /*end overloaded*/
					
					if (nodes.get(i).isRelayNode) {
						j = this.pickNeighbor(nodes, i);
						//nodes.get(j).setVisited(i);
						nodes.get(j).isRelayNode = true;
						DistributedOur.logger.debug("Handling relay node " + i + " " + nodes.get(i).markedBy);
						DistributedOur.logger.debug("Selected new relay node " + j);
						//pick oldest marker (they are sorted by their age)
						for (int kk=0;kk<nodes.get(i).markedBy.size(); kk++) {
							int index = nodes.get(i).markedBy.get(kk);
		
							this.attemptRelocation(index, j, time, false);
							
							//if we manage to relieve the load
							if (nodes.get(index).computeLoad(time, index) <= SystemSettings.getSystemSettings().getMax_node_load_threshold()) {
								//reset its status to running
								nodes.get(index).startedSearch = false;
								//and remove its print from i
								nodes.get(i).removeVisited(index);
								DistributedOur.logger.debug("Releaved node (via relay) " + index +"->" + j);
							}
						}
						//the neighbor should receive all prints from i
						nodes.get(j).addMarked(nodes.get(i).markedBy);
						//and i should forget about them
						nodes.get(i).markedBy.clear();
						//and continue running
						nodes.get(i).isRelayNode = false;
						DistributedOur.logger.debug("Unselected relay node " + i);
					}

			nodes.get(j).history.put(time,
			nodes.get(j).new NodeHistory(nodes.get(j).computeLoad(time, j), 
					AlgorithmUtil.getNoComponentTypes(nodes.get(j)), 
					AlgorithmUtil.computeNodeCost(nodes.get(j), nodes), 
					AlgorithmUtil.computeNumberServiceTypes(nodes.get(j)), 
					nodes.get(j).startedSearch,
					nodes.get(j).isRelayNode,
					nodes.get(j).isStopped));
			nodes.get(i).history.put(time,
			nodes.get(i).new NodeHistory(nodes.get(i).computeLoad(time, i), 
					AlgorithmUtil.getNoComponentTypes(nodes.get(i)), 
					AlgorithmUtil.computeNodeCost(nodes.get(i), nodes), 
					AlgorithmUtil.computeNumberServiceTypes(nodes.get(i)), 
					nodes.get(i).startedSearch,
					nodes.get(i).isRelayNode,
					nodes.get(i).isStopped));
			
			DistributedOur.logger.info("Node: " + i + "\tTime: " + 
								time + "\t Load: " + 
								nodes.get(i).history.get(time).getLoad() + "\t#Service Types: " + 
								nodes.get(i).history.get(time).getNoServiceTypes() + "\tOverloaded: " + 
								nodes.get(i).history.get(time).isOverloaded()+ "\tRelay node: " + 
								nodes.get(i).history.get(time).isRelayNode()+ "\tStopped: " + 
								nodes.get(i).history.get(time).isStopped());

		}
		return nodes;
	}
	
	/**
	 * Method for migrating components from an overloaded node to another
	 * The method tries to first move components that do not exist on the destination node
	 * while keeping the number of components still remaining on the source node > 1
	 * @param index the source node
	 * @param j the destination node
	 * @param time the time at which the move takes place
	 */
	public void attemptRelocation(int index, int j, int time, boolean underloaded) {
		int iterS = 0, iterSJ = 0, noServicesJ = 0, noServicesI = 0;
		Partition p;
		boolean allComponentsPresentOnJ = false;

		String[] sJ = AlgorithmUtil.computeNumberServiceTypes(nodes.get(j)).split("#");
		iterSJ = 0;
		//search for missing service types on j									
	    do {
	    	noServicesJ = Integer.parseInt(sJ[iterSJ++]);
	    } while (noServicesJ > 0 && iterSJ < sJ.length);
	    
	    if (iterSJ == sJ.length)
	    	allComponentsPresentOnJ = true;						    
	    iterSJ = 0;
	    
	    boolean conditionIndex = nodes.get(index).computeLoad(time, index) > SystemSettings.getSystemSettings().getMax_node_load_threshold();
	    if (underloaded)
	    	conditionIndex = nodes.get(index).computeLoad(time, index) < SystemSettings.getSystemSettings().getMin_node_load_threshold();
	    
		while  (nodes.get(j).computeLoad(time, j) < SystemSettings.getSystemSettings().getMax_node_load_threshold() 
				&& conditionIndex
				&& iterS < SystemSettings.getSystemSettings().getNo_component_types()) {
			
				String[] sIndex = AlgorithmUtil.computeNumberServiceTypes(nodes.get(index)).split("#");

				//only if there is a missing component type on j we attempt to relocate it from index
				//otherwise just try to relocate each type from index to j
				if (!allComponentsPresentOnJ && iterSJ < sJ.length) {
					//search for missing service types on j									
				    do {
				    	noServicesJ = Integer.parseInt(sJ[iterSJ]);
				    	iterSJ++;
				    } while (noServicesJ > 0 && iterSJ < sJ.length);
				    //if found missing type relocate the same component type from index
					if (noServicesJ <= 0)
						iterS = iterSJ - 1;
				}
				
				/*if (noServicesJ <=0) {
					System.out.println(AlgorithmUtil.computeNumberServiceTypes(nodes.get(index)) + " " + AlgorithmUtil.computeNumberServiceTypes(nodes.get(j)));
					System.out.println(noServicesJ + " " + Integer.parseInt(sIndex[iterS]));
				}*/
													
				noServicesI = Integer.parseInt(sIndex[iterS]);

				// if component exists on index move it to j
				if (noServicesI > 1) {
					for (int k=0; k<nodes.get(index).getAssignedPartitions().size(); k++)
						if (nodes.get(index).getAssignedPartitions().get(k).getAssignedComponents().get(0).getType() == iterS) {
							p = nodes.get(index).getAssignedPartitions().get(k);
							DistributedOur.logger.debug("Trying to move component type " + iterS + " from node " + index + " -> " + j);
							DistributedOur.logger.debug("New load on " + j + " : " + (nodes.get(j).computeLoad(time, j) + p.computeLoad(time)));
							if (conditionIndex) {
								if ((!underloaded && noServicesI-- >= 1) || (underloaded && noServicesI > 0)) {
									nodes.get(index).removePartition(p);
									p.assignToNode(nodes.get(j).getID());
									nodes.get(j).addPartition(p);
									DistributedOur.logger.debug("Moved component " + iterS + " from node " + index + " -> " + j);
								}
							}
						}
				}
				else //increment component type index on j if there is also no corresponding type on index
					if (!allComponentsPresentOnJ) { 
						iterSJ++;
					}
				iterS++;
		}	
		}
	
	public int pickNeighbor(Vector<Node> nodes, int i) {
		int j;
		do {
			do {
				j=this.graph.neighbors(i)[(int)(Math.random() * (this.graph.neighbors(i).length-1))];
			}
			while (j >= nodes.size());
		}
		while (nodes.get(j).isStopped);
		return j;
	}
		
	private void assignComponents(int[] componentsToBeGenerated) {
		Component component;
		Node node;
		Partition p;
		for (int i=0; i<componentsToBeGenerated.length; i++) {
			//only assigns components if value is positive
			//for negative value we should remove them
			for (int k=0; k<componentsToBeGenerated[i]; k++) {
				component = new Component(false, i);
				//link to other components as indicated by the relationship matrix
				for (int j=0; j<SystemSettings.getSystemSettings().getNo_component_types(); j++) {
					if (SystemSettings.getSystemSettings().getComponent_connection_table()[i][j] == 1) {
						component.addConnection(AlgorithmUtil.getRandomComponent(j, this.nodes));
					}
				}
				
				node = this.primaryNodes.get((int)(this.primaryNodes.size() * Math.random()));
				p = Partition.provide(node.getID());
				p.addComponent(component);
				node.addPartition(p);				
			}
		}
	}
	
	public void synchronize(Vector<Node> nodes, int i) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method for selecting the primary spawning nodes. These nodes represent the entry points for new components 
	 * @return the vector of spawning nodes
	 */
	private Vector<Node> selectPrimarySpawningNodes() {
		Vector<Node> primaryNodes = new Vector<Node>();
		
		for (int i=0; i<DistributedOur.NO_PRIMARY_NODES; i++)
			primaryNodes.add(this.nodes.get((int)(this.nodes.size() * Math.random())));
		
		return primaryNodes;
	}
}
