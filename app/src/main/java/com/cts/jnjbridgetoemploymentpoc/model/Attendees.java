package com.cts.jnjbridgetoemploymentpoc.model;

import java.io.Serializable;

/**
 * 
 * Provides Attendees model class Used while parsing Attendees JSON data
 * 
 * @author neerajareddy
 * 
 */

@SuppressWarnings("serial")
public class Attendees implements Serializable {

	private String name;
	private String rsvp_status;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRsvp_status() {
		return rsvp_status;
	}

	public void setRsvp_status(String rsvp_status) {
		this.rsvp_status = rsvp_status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
