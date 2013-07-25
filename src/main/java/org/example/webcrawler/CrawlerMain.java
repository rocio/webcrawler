package org.example.webcrawler;

import com.amazonaws.services.sqs.model.Message;
import org.example.webcrawler.queue.DocumentIndexer;
import org.example.webcrawler.queue.DocumentIndexerImpl;
import org.example.webcrawler.queue.QueueService;
import org.example.webcrawler.queue.QueueServiceImpl;
import org.example.webcrawler.queue.model.CrawledDocument;

import java.util.Iterator;
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

		QueueService queueService = new QueueServiceImpl();
		DocumentIndexer documentIndexer = new DocumentIndexerImpl();

		List<Message> messages = queueService.consume();

		for(Message message: messages) {
			System.out.println("[messageId]" + message.getMessageId());

			CrawledDocument document = documentIndexer.fetchDocument(message);
			if (document != null) {
				documentIndexer.indexDocument(document);
				queueService.produce(document.getChildUrl());
			}
		}
	}
}
