package mosaic.scheduler.platform.com.json.beans;

import java.util.Vector;

public class NodeListBean {
	private String node_id;
	private Vector<ComponentsBean> components;
	
	public NodeListBean(String nodeID, Vector<ComponentsBean> components) {
		this.node_id = nodeID;
		this.components = components;
	}

	public String getNode_id() {
		return node_id;
	}

	public Vector<ComponentsBean> getComponents() {
		return components;
	}
}
