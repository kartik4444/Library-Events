package com.Library.Events.LibraryEventsProducer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.Library.Events.LibraryEventsProducer.model.LibraryEvent;

@Controller
public class LibraryEventsController {
	Logger logger =LoggerFactory.getLogger(LibraryEventsController.class);
	@PostMapping(value = "v1/publishLibraryEvent")
	public ResponseEntity<String> produceLibraryEvent(@RequestBody LibraryEvent libraryEvent){
		logger.info("Producing Library Events to topic Library-Events");
		
		return new ResponseEntity<String>("Library event Produced", HttpStatus.CREATED);
	}

}
