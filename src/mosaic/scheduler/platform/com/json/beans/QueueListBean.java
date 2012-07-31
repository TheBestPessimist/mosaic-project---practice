package mosaic.scheduler.platform.com.json.beans;

public class QueueListBean {
	private String queue_id, no_messages;
	
	public QueueListBean(String queueID, String noMessages) {
		this.queue_id = queueID;
		this.no_messages = noMessages;	
	}

	public String getQueue_id() {
		return queue_id;
	}

	public String[] getNo_messages() {
		return no_messages.split(",");
	}
}
