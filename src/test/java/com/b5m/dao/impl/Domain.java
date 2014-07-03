package com.b5m.dao.impl;

import com.b5m.dao.annotation.Id;

public class Domain {
	@Id
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
