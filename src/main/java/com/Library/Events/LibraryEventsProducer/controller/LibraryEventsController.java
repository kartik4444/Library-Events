package com.Library.Events.LibraryEventsProducer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.Library.Events.LibraryEventsProducer.model.LibraryEvent;
import com.Library.Events.LibraryEventsProducer.model.LibraryEventType;
import com.Library.Events.LibraryEventsProducer.producer.LibraryEventProducer;

@Controller
public class LibraryEventsController {
	Logger logger = LoggerFactory.getLogger(LibraryEventsController.class);

	@Autowired
	private LibraryEventProducer libraryEventProducer;

	/*
	 * EndPoint to Asynchronously produce message to Kafka topic
	 */
	@PostMapping(value = "/v1/publishLibraryEventAsync1")
	public ResponseEntity<String> produceLibraryEventAsync(@RequestBody LibraryEvent libraryEvent) throws Exception {
		logger.info("Asynchronuosly Producing Library Events to topic Library-Events");
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		libraryEventProducer.postLibraryEvent(libraryEvent);
		return new ResponseEntity<String>(libraryEvent.toString(), HttpStatus.CREATED);
	}

	/*
	 * Endpoint to ASynchronously produce message to Kafka topic. It uses
	 * producerRecord approach to send events to kafka
	 */

	@PostMapping(value = "/v1/publishLibraryEventASync2")
	public ResponseEntity<String> postLibraryEventAsyncProdRecord(@RequestBody LibraryEvent libraryEvent)
			throws Exception {
		logger.info("ASynchronuosly Producing Library Events to topic Library-Events");
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		libraryEventProducer.postLibraryEventAsyncProdRecord(libraryEvent);
		logger.info("After produceLibraryEvent");
		return new ResponseEntity<String>(libraryEvent.toString(), HttpStatus.CREATED);
	}

	/*
	 * EndPoint to Synchronously produce message to Kafka topic
	 */

	@PostMapping(value = "/v1/publishLibraryEventSync")
	public ResponseEntity<String> produceLibraryEventSync(@RequestBody LibraryEvent libraryEvent) throws Exception {
		logger.info("Synchronuosly Producing Library Events to topic Library-Events");
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		SendResult<Integer, String> sendResult = libraryEventProducer.postLibraryEventSynchronous(libraryEvent);
		logger.info("After produce LibraryEvent ,Result {}", sendResult);
		return new ResponseEntity<String>(sendResult.toString(), HttpStatus.CREATED);
	}

}
