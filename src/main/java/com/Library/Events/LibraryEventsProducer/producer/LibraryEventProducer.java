package com.Library.Events.LibraryEventsProducer.producer;

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
	Logger logger =LoggerFactory.getLogger(LibraryEventProducer.class);
	
	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key=libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent); 
	ListenableFuture<SendResult<Integer, String>> listenableFuture= kafkaTemplate.sendDefault(key, value);
	listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

		@Override
		public void onSuccess(SendResult<Integer, String> result) {
			handleSuccess(key,value,result);
		}

		@Override
		public void onFailure(Throwable ex) {
			handleFailure(key,value,ex);
			
		}
	});
	}
	
	
	public void handleFailure(Integer key, String value, Throwable ex) {
   logger.error("Error sending message to topic:: exception is {}",ex);
   try {
	   throw ex;
   }
   catch(Throwable e) {
	   logger.error("Exception occurred in sending message ",e);
   }
		
	}



	public void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		logger.info("Message sent Successfully for key {} and value {} , partition {}",key,value,result.getRecordMetadata().partition());
			
	}
	
}
