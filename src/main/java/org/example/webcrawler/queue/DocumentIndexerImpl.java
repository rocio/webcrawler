package org.example.webcrawler.queue;

import com.amazonaws.services.sqs.model.Message;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.example.webcrawler.queue.model.CrawledDocument;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * author: rocio
 */
public class DocumentIndexerImpl implements DocumentIndexer {

	private static final String SOLR_SERVER = "http://localhost:4040/crawler";

	private SolrServer solrServer;
	private static final Log logger = LogFactory.getLog(DocumentIndexerImpl.class);

	public DocumentIndexerImpl() {
	 	solrServer = new ConcurrentUpdateSolrServer(SOLR_SERVER, 20, 20);
	}

	@Override
	public CrawledDocument fetchDocument(Message message) {

		if (message != null) {
			String url = message.getBody();
			if (StringUtils.isNotBlank(url)) {
				try {
					logger.info("[fetchDocument] " + url);
					Document document = Jsoup.connect(url).get();
					if (document != null) {
						Element body = document.body();
						Elements links = document.select("a[href]");

						CrawledDocument crawledDocument = new CrawledDocument();
						crawledDocument.setMessageId(message.getMessageId());
						crawledDocument.setMessageBody(body.text());
						crawledDocument.setChildUrl(getValidChildrenUrls(links));
						crawledDocument.setId(url);
						return crawledDocument;
					}

				} catch (IOException exception) {
					logger.error("[fetchDocument] ", exception);
				}
			}
		}
		return null;
	}

	@Override
	public void indexDocument(CrawledDocument crawledDocument) {
		if (crawledDocument == null) return;
		try {
			UpdateResponse updateResponse = solrServer.addBean(crawledDocument);
			logger.info(updateResponse.getResponse());
			solrServer.commit();
		} catch (SolrServerException solrEx) {
			logger.info("[index]", solrEx);
		} catch (IOException ioEx) {
			logger.info("[index]", ioEx);
		}
	}

	private List<String> getValidChildrenUrls(Elements links) {
		List<String> childrenUrls = Lists.newArrayListWithExpectedSize(links.size());
		for (Element element : links) {
			Attributes attributes = element.attributes();
			for (Attribute attribute : attributes) {
				if ("href".equals(attribute.getKey())) {
					String childUrl = attribute.getValue();
					if (isValidUrl(childUrl)) {
						childrenUrls.add(childUrl);
					}
				}
			}
		}
		return childrenUrls;
	}

	private boolean isValidUrl(String url) {
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			//TODO: JULIAN ---> fix me
			return false;
		}
		return true;
	}
}
