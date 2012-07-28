package mosaic.scheduler.platform.com.json.beans;

public class ComponentsBean {
	private int component_type;
	private int component_number;
	
	public ComponentsBean (int componentType, int componentNumber) {
		this.component_number = componentNumber;
		this.component_type = componentType;
	}

	public int getComponent_type() {
		return component_type;
	}

	public int getComponent_number() {
		return component_number;
	}
	
	
}
