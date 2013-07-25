package org.example.webcrawler;

import com.amazonaws.services.sqs.model.Message;
import org.example.webcrawler.queue.QueueServiceImpl;

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
		queueService.produce("Some message");

		Message message = queueService.consume();
		if (message != null) {
        	System.out.println("[messageId]" + message.getMessageId());
		}
    }
}
