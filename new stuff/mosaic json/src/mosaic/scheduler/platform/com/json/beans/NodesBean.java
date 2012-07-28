package mosaic.scheduler.platform.com.json.beans;

public class NodesBean {
	private String node_id, node_load, node_datacenter_id, node_cloud_id;
	
	public NodesBean (String nodeID, String nodeDatacenterID, String nodeCloudID, String nodeLoad) {
		this.node_id = nodeID;
		this.node_load = nodeLoad;
		this.node_datacenter_id = nodeDatacenterID;
		this.node_cloud_id = nodeCloudID;
	}

	public String getNode_id() {
		return node_id;
	}

	public String getNode_load() {
		return node_load;
	}

	public String getNode_datacenter_id() {
		return node_datacenter_id;
	}

	public String getNode_cloud_id() {
		return node_cloud_id;
	}	
}
