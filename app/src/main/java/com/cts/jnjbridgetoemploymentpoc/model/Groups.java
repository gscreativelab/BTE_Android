package com.cts.jnjbridgetoemploymentpoc.model;

import java.io.Serializable;

/**
 * Provide Groups model class used while parsing Groups JSON data
 * 
 * @author neerajareddy
 * 
 */
@SuppressWarnings("serial")
public class Groups implements Serializable {

	private String name;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
