package com.accenture.client.dto;

import org.springframework.context.ApplicationEvent;

public class OrderServiceEvent extends ApplicationEvent {

	public OrderServiceEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
