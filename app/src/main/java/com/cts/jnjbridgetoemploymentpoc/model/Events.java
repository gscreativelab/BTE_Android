package com.cts.jnjbridgetoemploymentpoc.model;

import java.io.Serializable;

/**
 * Provides Events model class Used while parsing Events JSON data
 * 
 * @author neerajareddy
 * 
 */
@SuppressWarnings("serial")
public class Events implements Serializable {
	private String id;
	private String start_time;
	private String end_time;
	private String timezone;
	private String name;
	private String location;
	private String created_time;
	private String from;
	private String message;
	private String organized_by;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrganized_by() {
		return organized_by;
	}

	public void setOrganized_by(String organized_by) {
		this.organized_by = organized_by;
	}
}
