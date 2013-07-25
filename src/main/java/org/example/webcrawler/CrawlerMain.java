package org.example.webcrawler;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.example.webcrawler.queue.DocumentIndexer;
import org.example.webcrawler.queue.DocumentIndexerImpl;
import org.example.webcrawler.queue.QueueServiceImpl;
import org.example.webcrawler.queue.model.CrawledDocument;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgraham
 * Date: 7/24/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerMain {
    public static void main(String[] argv) {

		QueueServiceImpl queueService = new QueueServiceImpl();
		DocumentIndexer documentIndexer = new DocumentIndexerImpl();

		List<Message> messages = queueService.consume();
		if (CollectionUtils.isNotEmpty(messages)) {
			for(Message message: messages) {
				System.out.println("[messageId]" + message.getMessageId());
				CrawledDocument document = documentIndexer.fetchDocument(message);
				documentIndexer.indexDocument(document);
				if(CollectionUtils.isNotEmpty(document.getChildUrl())) {
					for (String url: document.getChildUrl()) {
						System.out.println(url);
						queueService.produce(url);
					}
				}
			}
		}
    }
}
