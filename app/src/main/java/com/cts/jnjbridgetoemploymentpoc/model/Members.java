package com.cts.jnjbridgetoemploymentpoc.model;

import java.io.Serializable;

/**
 * 
 * Provides Members model class Used while parsing Members JSON data
 * 
 * @author neerajareddy
 * 
 */

@SuppressWarnings("serial")
public class Members implements Serializable {
	private String id;
	private String administrator;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
