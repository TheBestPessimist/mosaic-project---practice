package mosaic.scheduler.platform.com.json.beans;

public class ComponentLoadListBean {
	private int component_type;
	private String component_load;
//	private Vector<QueueListBean> queue_list;
	
	public ComponentLoadListBean(int componentType, String componentLoad) {
		this.component_load = componentLoad;
		this.component_type = componentType;
//		this.queue_list = queueList;
	}

	public int getComponent_type() {
		return component_type;
	}

	public String getComponent_load() {
		return component_load;
	}

//	public Vector<QueueListBean> getQueueList() {
//		return queue_list;
//	}
	
}
