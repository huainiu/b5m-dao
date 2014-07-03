package com.b5m.dao.impl;

import com.b5m.dao.annotation.Column;
import com.b5m.dao.annotation.NamedQueries;
import com.b5m.dao.annotation.NamedQuery;
import com.b5m.dao.annotation.Table;

@Table("t_person")
@NamedQueries({
	@NamedQuery(name="selectId", sql="SELECT id FROM t_person where id = ?"),
	@NamedQuery(name="selectId1", sql="SELECT id FROM t_person")
})
public class Person extends Domain{
	
	@Column(name = "name", length = 20)
	private String name;
	
	@Column
	private int age;
	
	@Column(name = "code")
	private String code;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age
				+ ", code=" + code + "]";
	}
	
}