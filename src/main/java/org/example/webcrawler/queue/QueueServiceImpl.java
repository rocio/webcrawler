package org.example.webcrawler.queue;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.webcrawler.queue.model.QueueMessageResponse;

import java.util.Collections;
import java.util.List;

/**
 * author: rocio
 */
public class QueueServiceImpl implements QueueService {

	public static final String QUEUE_URL= "https://sqs.us-east-1.amazonaws.com/031396681695/julian-testHtmlCrawlingQueue";

	private AmazonSQSClient sqsClient;

	public QueueServiceImpl() {
		sqsClient = new AmazonSQSClient(new AnonymousAWSCredentials());
	}

	@Override
	public List<Message> consume() {
		ReceiveMessageRequest messageRequest = new ReceiveMessageRequest()
				.withQueueUrl(QUEUE_URL)
				.withMaxNumberOfMessages(10);

		ReceiveMessageResult messageResult = sqsClient.receiveMessage(messageRequest);
		if (messageResult != null && CollectionUtils.isNotEmpty(messageResult.getMessages())) {
			return messageResult.getMessages();
			//return deleteMessage(message);
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	public QueueMessageResponse produce(String message) {
		if (StringUtils.isNotBlank(message)) {
			SendMessageRequest messageRequest = new SendMessageRequest()
					.withQueueUrl(QUEUE_URL)
					.withMessageBody(message);

			SendMessageResult messageResult = sqsClient.sendMessage(messageRequest);
			if (messageRequest != null) {
				QueueMessageResponse queueMessageResponse = new QueueMessageResponse();
				queueMessageResponse.setMessageId(messageResult.getMessageId());
				return queueMessageResponse;
			}
		}

		return new QueueMessageResponse("FAIL");
	}

	@Override
	public List<QueueMessageResponse> produce(List<String> messages) {
		List<QueueMessageResponse> response = Lists.newArrayListWithExpectedSize(messages.size());
		for (String message: messages) {
			response.add(produce(message));
		}
		return Collections.EMPTY_LIST;
	}

	private Message deleteMessage(Message message) {
		sqsClient.deleteMessage(new DeleteMessageRequest().withQueueUrl(QUEUE_URL).withReceiptHandle(message.getReceiptHandle()));
		return message;
	}
}
