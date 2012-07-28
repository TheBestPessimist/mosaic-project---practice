package mosaic.scheduler.platform.com.json.beans;

public class ComponentWorkflowBean {
	private int component_type;
	private String read_rate, write_rate;
	private String linked_to_component;
	
	public ComponentWorkflowBean(int componentID, String linkedToComponent, String writeRate, String readRate) {
		this.component_type = componentID;
		this.linked_to_component = linkedToComponent;
		this.read_rate = readRate;
		this.write_rate = writeRate;
	}

	public int getComponent_type() {
		return component_type;
	}

	public String[] getLinked_to_Component() {
		return linked_to_component.split(",");
	}
	
	public String[] getRead_rate() {
		return read_rate.split(",");
	}

	public String[] getWrite_rate() {
		return write_rate.split(",");
	}
	
	
}
