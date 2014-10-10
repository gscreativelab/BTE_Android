package com.cts.jnjbridgetoemploymentpoc.model;

/**
 * Provides Announcements model class Used while parsing Announcements JSON data
 * 
 * @author neerajareddy
 * 
 */
public class Announcements {

	private String title;
	private String date;
	private String description;
	private String id;
	private String name;
	private String start_time;

	public Announcements() {
	}

	public Announcements(String title, String date, String description) {
		super();
		this.title = title;
		this.date = date;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
}
