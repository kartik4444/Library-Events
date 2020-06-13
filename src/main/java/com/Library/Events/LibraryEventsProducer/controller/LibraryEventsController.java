package com.Library.Events.LibraryEventsProducer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.Library.Events.LibraryEventsProducer.model.LibraryEvent;
import com.Library.Events.LibraryEventsProducer.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class LibraryEventsController {
	Logger logger =LoggerFactory.getLogger(LibraryEventsController.class);
	
	@Autowired
	private LibraryEventProducer libraryEventProducer;
	
	@PostMapping(value = "/v1/publishLibraryEvent")
	public ResponseEntity<String> produceLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException{
		logger.info("Producing Library Events to topic Library-Events");
		libraryEventProducer.sendLibraryEvent(libraryEvent);
		return new ResponseEntity<String>(libraryEvent.toString(), HttpStatus.CREATED);
	}

}
