package mosaic.scheduler.test;

import java.util.Vector;

import mosaic.scheduler.platform.resources.ComponentRequirementsList;
import mosaic.scheduler.platform.resources.Node;
import mosaic.scheduler.platform.settings.SystemSettings;
import mosaic.scheduler.simulator.algorithms.DistributedOur;
import mosaic.scheduler.simulator.resources.ComponentRequirements;
import mosaic.scheduler.simulator.scaler.Scaler;
import mosaic.scheduler.simulator.util.Runner;
import mosaic.scheduler.simulator.util.math.Probability;
import mosaic.scheduler.simulator.util.math.Statistics;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class TestDistributedOur {
	private static Logger logger = Logger.getLogger(Runner.class.getPackage().getName());

	public static int NO_NODES = 10;
	public static int LOOP_NO = 3;
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("logging.properties");
		
		SystemSettings.getSystemSettings().loadProperties("mosaic/scheduler/simulator/settings/system.properties");

		DistributedOur so = new DistributedOur();
		
		Vector<Node> nodes = new Vector<Node>();
		for (int i=0; i< TestDistributedOur.NO_NODES; i++)
			nodes.add(new Node("1", "1"));
		
		for (int i=0; i<SystemSettings.getSystemSettings().getNo_component_types(); i++) {
			ComponentRequirementsList crl  = ComponentRequirementsList.getComponentsRequirement();
			crl.addComponentRequirement(i, new ComponentRequirements());
		}
		
		Scaler scaler = new Scaler();
		scaler.assignAlgorithm(so);
		
		double time, noNewWebServers;
		int[] components;
		long s = System.currentTimeMillis();
		
		double percentageNotHAFinal[] = new double[TestDistributedOur.LOOP_NO];
	
		for (int t = 0; t<TestDistributedOur.LOOP_NO; t++) {
			/*time = (double)t / SystemSettings.getSystemSettings().getTime_span() - 0.5;
			noNewWebServers = 3.1 - 8.5 * time + 24.7 * Math.pow(time, 2) + 130.8
			* Math.pow(time, 3) + 107.7 * Math.pow(time, 4) - 804.2
			* Math.pow(time, 5) - 2038.5 * Math.pow(time, 6) + 1856.8
			* Math.pow(time, 7) + 4618.6 * Math.pow(time, 8);
			*/
			noNewWebServers = Probability.generatePareto(1.75, 1.68);
		
			//if (t < 10) {
				components = scaler.scaleComponents((int)noNewWebServers, nodes);
			//	System.out.println("time " + t + " " + (int)noNewWebServers);
			//	for (int i : components)
			//		System.out.print (i + " ");
			//	System.out.println();
			//}
			//else {
		//		components = new int[SystemSettings.getSystemSettings().getNo_component_types()];
		//	}
			nodes = so.executeOnce(nodes, components, t % (SystemSettings.getSystemSettings().getTime_span() == Integer.MAX_VALUE ? 24 : SystemSettings.getSystemSettings().getTime_span()));
			
		}
		TestDistributedOur.logger.debug("Execution time: " + (System.currentTimeMillis() - s));
		double percentageNotHA = 0;
		for (int i=0; i<TestDistributedOur.LOOP_NO; i++) {
			percentageNotHA = 0;
			for (int j=0; j< nodes.size(); j++) {
//				if (nodes.get(j).history.get(i)!=null)
					TestDistributedOur.logger.info("Time: " + i + "\tNode: " + j + "\tLoad: " + nodes.get(j).history.get(i).getLoad() + "\t#Service types: " + nodes.get(j).history.get(i).getNoServiceTypes() + "\tStarted search: " + nodes.get(j).history.get(i).isOverloaded()+ "\tRelay node: " + nodes.get(j).history.get(i).isRelayNode() + "\tStopped: " + nodes.get(j).history.get(i).isStopped());
				//Compute the number of nodes that do not have HA at time i
				if (nodes.get(j).history.get(i).getNoServiceTypes() != SystemSettings.getSystemSettings().getNo_component_types() && nodes.get(j).history.get(i).getLoad() > 0)
					percentageNotHA++;
			}
			//compute and store the percentage of nodes without HA at time i
			percentageNotHAFinal[i] = percentageNotHA * 100 / nodes.size();
			System.out.println(percentageNotHA* 100 / nodes.size());
		}
				
		System.out.println(Statistics.computeMean(percentageNotHAFinal) + "\t" + Statistics.computeStandardDeviation(percentageNotHAFinal));		
	}
	
}
