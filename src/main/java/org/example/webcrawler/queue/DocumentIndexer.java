package org.example.webcrawler.queue;

import com.amazonaws.services.sqs.model.Message;
import org.example.webcrawler.queue.model.CrawledDocument;

/**
 * author: rocio
 */
public interface DocumentIndexer {

	CrawledDocument fetchDocument(Message message);

	void indexDocument(CrawledDocument crawledDocument);
}
