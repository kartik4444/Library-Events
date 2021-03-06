package com.Library.Events.LibraryEventsProducer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LibraryEvent {

	private  Integer libraryEventId;
	private LibraryEventType libraryEventType;
	private   Book    book;
	
	
}
