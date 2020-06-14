package com.Library.Events.LibraryEventsProducer.producer;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.Library.Events.LibraryEventsProducer.model.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LibraryEventProducer {
	Logger logger = LoggerFactory.getLogger(LibraryEventProducer.class);

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	/*
	 * To Send Library Events ASynchronously to Kafka topic. User will get created
	 * response as soon as an library event is scanned irrespective of sending is
	 * success or failed
	 */

	public void postLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				logger.info("Message sent Successfully for key {} and value {} , partition {}", key, value,
						result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);

			}
		});
	}

	public void handleFailure(Integer key, String value, Throwable ex) {
		logger.error("Error sending message to topic:: exception is {}", ex);
		try {
			throw ex;
		} catch (Throwable e) {
			logger.error("Exception occurred in sending message ", e);
		}

	}

	/*
	 * To Send Library Events Synchronously to Kafka topic User will get created
	 * response only when it is successfully send to kafka topic
	 */

	public SendResult<Integer, String> postLibraryEventSynchronous(LibraryEvent libraryEvent) throws Exception {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		SendResult<Integer, String> sendResult = null;
		try {
			sendResult = kafkaTemplate.sendDefault(key, value).get();
		} catch (ExecutionException | InterruptedException e) {
			logger.error("ExecutionException/InterruptedException occurred in sending event to kafka,exception is {}",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred in sending event to kafka,exception is {}", e.getMessage());
			throw e;

		}
		return sendResult;

	}

}
