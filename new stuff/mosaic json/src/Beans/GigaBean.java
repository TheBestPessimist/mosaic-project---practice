package Beans;

/**
 * This is the bean that holds the other 2 beans: FromMachineBean and ToMachineBean
 * @author The Best Pessimist
 */
public class GigaBean {
	
	FromMachineBean from_machine = new FromMachineBean();
	ToMachineBean to_machine = new ToMachineBean();
	/**
	 * @return the from_machine
	 */
	public FromMachineBean getFrom_machine() {
		return from_machine;
	}
	/**
	 * @param from_machine the from_machine to set
	 */
	public void setFrom_machine(FromMachineBean from_machine) {
		this.from_machine = from_machine;
	}
	/**
	 * @return the to_machine
	 */
	public ToMachineBean getTo_machine() {
		return to_machine;
	}
	/**
	 * @param to_machine the to_machine to set
	 */
	public void setTo_machine(ToMachineBean to_machine) {
		this.to_machine = to_machine;
	}
	
	
}
