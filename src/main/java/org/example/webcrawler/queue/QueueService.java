package org.example.webcrawler.queue;

import com.amazonaws.services.sqs.model.Message;
import org.example.webcrawler.queue.model.QueueMessageResponse;

import java.util.List;

/**
 * author: rocio
 */
public interface QueueService {

	Message consume();

  	QueueMessageResponse produce(String message);

}
