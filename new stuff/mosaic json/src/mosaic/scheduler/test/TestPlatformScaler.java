package mosaic.scheduler.test;

import java.util.Vector;

import mosaic.scheduler.platform.Scaler;
import mosaic.scheduler.platform.com.json.beans.ComponentWorkflowBean;
import mosaic.scheduler.platform.com.json.beans.ComponentsBean;
import mosaic.scheduler.platform.com.json.beans.NodeListBean;
import mosaic.scheduler.platform.com.json.beans.QueueListBean;
import mosaic.scheduler.platform.settings.SystemSettings;

import org.apache.log4j.PropertyConfigurator;

public class TestPlatformScaler {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("logging.properties");

		SystemSettings
				.getSystemSettings()
				.loadProperties(
						"mosaic/scheduler/platform/settings/system.properties.platform");

		Scaler s = new Scaler();

		Vector<ComponentWorkflowBean> componentWorkflow;
		Vector<NodeListBean> crtNodes;
		Vector<QueueListBean> queue;
		
		// component workflow
		componentWorkflow = new Vector<ComponentWorkflowBean>();
		componentWorkflow.add(new ComponentWorkflowBean(0, "1,2", "20,20", "10,10"));
		componentWorkflow.add(new ComponentWorkflowBean(1, "0,3", "10,10", "10,5"));
		componentWorkflow.add(new ComponentWorkflowBean(2, "0,3", "10,10", "5,5"));
		componentWorkflow.add(new ComponentWorkflowBean(3, "1,2,4", "10,5,5", "5,5,10"));
		componentWorkflow.add(new ComponentWorkflowBean(4, "3", "5", "5"));
		
		// node list
		crtNodes = new Vector<NodeListBean>();
		
		Vector<ComponentsBean> components = new Vector<ComponentsBean>();
		components.add(new ComponentsBean(0, 1));
		components.add(new ComponentsBean(1, 1));
		components.add(new ComponentsBean(2, 1));
		components.add(new ComponentsBean(3, 1));
		components.add(new ComponentsBean(4, 1));
		
		crtNodes.add(new NodeListBean("1", components));
		queue = new Vector<QueueListBean>();
		queue.add(new QueueListBean("0-1", "40"));
		queue.add(new QueueListBean("0-2", "60"));
		queue.add(new QueueListBean("1-0", "20"));
		queue.add(new QueueListBean("1-3", "20"));
		queue.add(new QueueListBean("2-0", "20"));
		queue.add(new QueueListBean("2-3", "20"));
		queue.add(new QueueListBean("3-1", "10"));
		queue.add(new QueueListBean("3-2", "5"));
		queue.add(new QueueListBean("3-4", "5"));
		queue.add(new QueueListBean("4-3", "15"));

		
		s.scale(componentWorkflow, queue, crtNodes);

	}
}
