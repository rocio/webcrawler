package org.example.webcrawler.queue;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.webcrawler.queue.model.QueueMessageResponse;

/**
 * author: rocio
 */
public class QueueServiceImpl implements QueueService {

	private static final String QUEUE_URL= "https://sqs.us-east-1.amazonaws.com/031396681695/julian-testHtmlCrawlingQueue";

	@Override
	public Message consume() {
		AmazonSQSClient sqsClient = new AmazonSQSClient(new AnonymousAWSCredentials());
		ReceiveMessageRequest messageRequest = new ReceiveMessageRequest()
				.withQueueUrl(QUEUE_URL)
				.withMaxNumberOfMessages(1);

		ReceiveMessageResult messageResult = sqsClient.receiveMessage(messageRequest);
		if (messageResult != null && CollectionUtils.isNotEmpty(messageResult.getMessages())) {
			Message message = messageResult.getMessages().get(0);
			return message;
		}


		return null;
	}

	@Override
	public QueueMessageResponse produce(String message) {
		if (StringUtils.isNotBlank(message)) {
			SendMessageRequest messageRequest = new SendMessageRequest()
					.withQueueUrl(QUEUE_URL)
					.withMessageBody(message);

			AmazonSQSClient sqsClient = new AmazonSQSClient(new AnonymousAWSCredentials());
			SendMessageResult messageResult = sqsClient.sendMessage(messageRequest);
			if (messageRequest != null) {
				QueueMessageResponse queueMessageResponse = new QueueMessageResponse();
				queueMessageResponse.setMessageId(messageResult.getMessageId());
				return queueMessageResponse;
			}
		}

		return new QueueMessageResponse("FAIL");
	}
}
