package mosaic.scheduler.simulator.resources;

import mosaic.scheduler.platform.resources.AComponentRequirements;
import mosaic.scheduler.simulator.util.math.Probability;

/**
 * Class that simulates the evolution of a component requirements
 * @author Marc Frincu
 *
 */
public class ComponentRequirements extends AComponentRequirements {
		
	//TODO read from a file the requirements as alternative
	
	public void generateCpuUsage() {
		double prob = Probability.BoxMuller(0, 50, 20, 15);
		for (int i=0; i<this.cpuUsage.length; i++) {
			do {
				this.cpuUsage[i] = prob;
			} while (this.cpuUsage[i] <= 0);			
		}
	}
	
	public void generateMemoryUsage() {
		double prob = Probability.BoxMuller(0, 10, 2, 1);		
		for (int i=0; i<this.memoryUsage.length; i++) {
			do {
				this.memoryUsage[i] = prob;
			} while (this.memoryUsage[i] <= 0);
		}
	}
	
	public void generateNetworkUsage() {
		double prob = Probability.BoxMuller(0, 10, 2, 1);
		for (int i=0; i<this.networkUsage.length; i++) {
			do {
				this.networkUsage[i] = prob;
			} while (this.networkUsage[i] <= 0);
		}
	}

	@Override
	public void setCpuUsage(int time, double usage) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void setMemoryUsage(int time, double usage) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void setNetworkUsage(int time, double usage) {
		throw new UnsupportedOperationException();
	}
}
