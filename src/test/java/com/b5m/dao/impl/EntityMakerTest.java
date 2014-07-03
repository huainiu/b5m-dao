package com.b5m.dao.impl;

import junit.framework.TestCase;

import com.b5m.dao.EntityMaker;
import com.b5m.dao.domain.item.InsertItem;
import com.b5m.dao.domain.item.UpdateItem;
import com.b5m.dao.domain.orderby.OrderBySet;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.impl.EntityMakerImpl;

public class EntityMakerTest extends TestCase{
	public void testMaker(){
		EntityMaker entityMaker = new EntityMakerImpl();
		Entity<Person> entity = entityMaker.make(Person.class);
		System.out.println(entity);
		
		Person person = new Person();
		InsertItem insertItem = new InsertItem(person);
		StringBuilder sb = new StringBuilder();
		insertItem.joinSql(entity, sb);
		System.out.println(sb.toString());
		
		UpdateItem updateItem = new UpdateItem(person);
		StringBuilder sb1 = new StringBuilder();
		updateItem.joinSql(entity, sb1);
		System.out.println(sb1.toString());
		
		
		StringBuilder sb2 = new StringBuilder();
		OrderBySet orderBySet = new OrderBySet();
		orderBySet.asc("name");
		orderBySet.desc("id");
		orderBySet.joinSql(entity, sb2);
		System.out.println(sb2.toString());
	}
}
