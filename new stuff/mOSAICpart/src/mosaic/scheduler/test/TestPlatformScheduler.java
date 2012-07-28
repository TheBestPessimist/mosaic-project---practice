package mosaic.scheduler.test;

import mosaic.scheduler.platform.Scheduler;
import mosaic.scheduler.platform.algorithms.OurOnetoOne;
import mosaic.scheduler.platform.resources.ComponentRequirementsList;
import mosaic.scheduler.platform.resources.ComponentRequirementsPlatform;
import mosaic.scheduler.platform.settings.SystemSettings;

import org.apache.log4j.PropertyConfigurator;


public class TestPlatformScheduler {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("logging.properties");

		SystemSettings.getSystemSettings().loadProperties("mosaic/scheduler/platform/settings/system.properties.platform");

		for (int i=0; i<SystemSettings.getSystemSettings().getNo_component_types(); i++) {
			ComponentRequirementsList crl  = ComponentRequirementsList.getComponentsRequirement();
			crl.addComponentRequirement(i, new ComponentRequirementsPlatform());
		}
		
		OurOnetoOne a = new OurOnetoOne(); 
		Scheduler s = new Scheduler(a);
			
		s.run();
	}

}
