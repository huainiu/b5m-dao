package com.b5m.dao.domain.cnd;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.domain.item.SqlExp;
import com.b5m.dao.domain.item.SqlExpsGroup;
import com.b5m.dao.domain.item.Static;
import com.b5m.dao.domain.orderby.OrderBySet;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class Cnd {
	private OrderBySet orderBySet;
	
	private SqlExpsGroup sqlExpsGroup;
	
	private boolean firstAdd = true;
	
	private Cnd(){
		this.orderBySet = new OrderBySet();
		sqlExpsGroup = new SqlExpsGroup();
	}
	
	public static Cnd _new(){
		Cnd cnd = new Cnd();
		return cnd;
	}
	
	public static Cnd where(String name, Op op, Object value){
		Cnd cnd = _new();
		cnd.add(name, op, value);
		return cnd;
	}
	
	public Cnd add(String name, Op op, Object value){
		if(value == null || StringUtils.isEmpty(value.toString())) return this;
		addAdd();
		sqlExpsGroup.and(SqlExp.newInstance(name, op, value));
		return this;
	}
	
	public Cnd or(String name, Op op, Object value){
		if(value == null || StringUtils.isEmpty(value.toString())) return this;
		addOr();
		sqlExpsGroup.and(SqlExp.newInstance(name, op, value));
		return this;
	}
	
	public static Cnd whereIn(String name, Object[] objs){
		Cnd cnd = _new();
		if(objs == null || objs.length < 1) return cnd;
		cnd.addAdd();
		cnd.getSqlExpsGroup().and(SqlExp.newInstance(name, Op.IN, objs));
		return cnd;
	}
	
	public Cnd andIn(String name, Object[] objs){
		if(objs == null || objs.length < 1) return this;
		addAdd();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.IN, objs));
		return this;
	}
	
	public Cnd orIn(String name, Object[] objs){
		if(objs == null || objs.length < 1) return this;
		addOr();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.IN, objs));
		return this;
	}
	
	public Cnd andBetween(String name, Object v1, Object v2){
		addAdd();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.BETWEEN, new Object[]{v1, v2}));
		return this;
	}
	
	public Cnd orBetween(String name, Object v1, Object v2){
		addOr();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.BETWEEN, new Object[]{v1, v2}));
		return this;
	}
	
	public static Cnd whereBetween(String name, Object v1, Object v2){
		Cnd cnd = _new();
		cnd.addAdd();
		cnd.getSqlExpsGroup().and(SqlExp.newInstance(name, Op.BETWEEN, new Object[]{v1, v2}));
		return cnd;
	}
	
	public static Cnd whereIsNull(String name){
		Cnd cnd = _new();
		cnd.addAdd();
		cnd.getSqlExpsGroup().and(SqlExp.newInstance(name, Op.ISNULL, null));
		return cnd;
	}
	
	public Cnd andIsNull(String name){
		addAdd();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.ISNULL, null));
		return this;
	}
	
	public Cnd orIsNull(String name){
		addOr();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.ISNULL, null));
		return this;
	}
	
	public static Cnd whereIsNotNull(String name){
		Cnd cnd = _new();
		cnd.addAdd();
		cnd.getSqlExpsGroup().and(SqlExp.newInstance(name, Op.ISNOTNULL, null));
		return cnd;
	}
	
	public Cnd andIsNotNull(String name){
		addAdd();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.ISNOTNULL, null));
		return this;
	}
	
	public Cnd orIsNotNull(String name){
		addOr();
		sqlExpsGroup.and(SqlExp.newInstance(name, Op.ISNOTNULL, null));
		return this;
	}
	
	public Cnd addSqlExpsGroup(SqlExpsGroup sqlExpsGroup){
		addAdd();
		sqlExpsGroup.addSqlExpsGroup(sqlExpsGroup);
		return this;
	}
	
	public Cnd orSqlExpsGroup(SqlExpsGroup sqlExpsGroup){
		addOr();
		sqlExpsGroup.addSqlExpsGroup(sqlExpsGroup);
		return this;
	}
	
	public Cnd desc(String name){
		orderBySet.desc(name);
		return this;
	}
	
	public Cnd asc(String name){
		orderBySet.asc(name);
		return this;
	}
	
	private void addAdd(){
		if(!firstAdd) sqlExpsGroup.and(new Static("AND"));
		else {
			sqlExpsGroup.and(new Static("WHERE"));
			firstAdd = false;
		}
	}
	
	private void addOr(){
		if(!firstAdd) sqlExpsGroup.and(new Static("OR"));
		else {
			sqlExpsGroup.and(new Static("WHERE"));
			firstAdd = false;
		}
	}
	
	public OrderBySet getOrderBySet() {
		return orderBySet;
	}

	public void setOrderBySet(OrderBySet orderBySet) {
		this.orderBySet = orderBySet;
	}

	public SqlExpsGroup getSqlExpsGroup() {
		return sqlExpsGroup;
	}

	public void setSqlExpsGroup(SqlExpsGroup sqlExpsGroup) {
		this.sqlExpsGroup = sqlExpsGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (firstAdd ? 1231 : 1237);
		result = prime * result
				+ ((orderBySet == null) ? 0 : orderBySet.hashCode());
		result = prime * result
				+ ((sqlExpsGroup == null) ? 0 : sqlExpsGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cnd other = (Cnd) obj;
		if (firstAdd != other.firstAdd)
			return false;
		if (orderBySet == null) {
			if (other.orderBySet != null)
				return false;
		} else if (!orderBySet.equals(other.orderBySet))
			return false;
		if (sqlExpsGroup == null) {
			if (other.sqlExpsGroup != null)
				return false;
		} else if (!sqlExpsGroup.equals(other.sqlExpsGroup))
			return false;
		return true;
	}

}
