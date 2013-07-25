package org.example.webcrawler.queue.model;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * author: rocio
 */
public class CrawledDocument {

	private String id;
	private String messageId;
	private String messageBody;
	private List<String> childUrl;

	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	@Field
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageBody() {
		return messageBody;
	}

	@Field
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public List<String> getChildUrl() {
		return childUrl;
	}

	@Field
	public void setChildUrl(List<String> childUrl) {
		this.childUrl = childUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CrawledDocument)) return false;

		CrawledDocument that = (CrawledDocument) o;

		if (childUrl != null ? !childUrl.equals(that.childUrl) : that.childUrl != null) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (messageBody != null ? !messageBody.equals(that.messageBody) : that.messageBody != null) return false;
		if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
		result = 31 * result + (messageBody != null ? messageBody.hashCode() : 0);
		result = 31 * result + (childUrl != null ? childUrl.hashCode() : 0);
		return result;
	}
}
