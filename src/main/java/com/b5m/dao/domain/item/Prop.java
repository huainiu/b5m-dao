package com.b5m.dao.domain.item;

public class Prop {
	private String name;

	private Object value;
	
	public static Prop newInstance(String name, Object value){
		return new Prop(name, value);
	}
	
	public Prop(String name, Object value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
