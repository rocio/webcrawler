package org.example.webcrawler.queue.model;

/**
 * author: rocio
 */
public class QueueMessageResponse {

	private String messageId;
	private String status = "SUCCESS";

	public QueueMessageResponse() {
	}

	public QueueMessageResponse(String status) {
		this.status = status;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
