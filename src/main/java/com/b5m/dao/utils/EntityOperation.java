package com.b5m.dao.utils;

import java.util.List;
import java.util.Map;

import com.b5m.dao.Dao;
import com.b5m.dao.domain.DaoStatement;
import com.b5m.dao.domain.SqlType;
import com.b5m.dao.domain.cnd.Cnd;
import com.b5m.dao.domain.item.Chain;
import com.b5m.dao.domain.item.CreateEntityItem;
import com.b5m.dao.domain.item.CreateItem;
import com.b5m.dao.domain.item.DeleteItem;
import com.b5m.dao.domain.item.DropItem;
import com.b5m.dao.domain.item.ExeSqlItem;
import com.b5m.dao.domain.item.InsertItem;
import com.b5m.dao.domain.item.QueryFromItem;
import com.b5m.dao.domain.item.QueryNameItem;
import com.b5m.dao.domain.item.QuerySqlItem;
import com.b5m.dao.domain.item.SelectCountItem;
import com.b5m.dao.domain.item.SelectItem;
import com.b5m.dao.domain.item.UpdateItem;
import com.b5m.dao.domain.page.PageCnd;
import com.b5m.dao.domain.table.Entity;
/**
 * @author echo
 */
@SuppressWarnings("unchecked")
public class EntityOperation<T> {
	private Dao dao;
	private Entity<T> en;
	private Object o;
	private Cnd cnd;
	private DaoStatement daoStatement;
	private PageCnd pageCnd;
	private Chain chain;
	
	public EntityOperation(Dao dao, Entity<T> en, Object o){
		this.dao = dao;
		this.en = en;
		this.o = o;
	}
	
	public EntityOperation(Dao dao, Entity<T> en, Cnd cnd){
		this.dao = dao;
		this.en = en;
		this.cnd = cnd;
	}
	
	public EntityOperation(Dao dao, Entity<T> en, Chain chain, Cnd cnd){
		this.dao = dao;
		this.en = en;
		this.cnd = cnd;
		this.chain = chain;
	}
	
	public void exec(){
		dao._exec(daoStatement);
	}
	
	public void addSelect(){
		daoStatement = new DaoStatement(SqlType.SELECT, en);
		daoStatement.addItem(SelectItem.newInstance());
		daoStatement.addItem(cnd.getSqlExpsGroup());
		daoStatement.addItem(cnd.getOrderBySet());
	}
	
	public void addSelectCount(){
		daoStatement = new DaoStatement(SqlType.SELECTCOUNT, en);
		daoStatement.addItem(SelectCountItem.newInstance());
		daoStatement.addItem(cnd.getSqlExpsGroup());
	}
	
	public void addInsert(){
		daoStatement = new DaoStatement(SqlType.ADD, en);
		daoStatement.addItem(InsertItem.newInstance(o));
		daoStatement.setOperationObject(o);
	}
	
	public void addBatchInsert(){
		daoStatement = new DaoStatement(SqlType.BATCH, en);
		daoStatement.addItem(InsertItem.newInstance(o));
		daoStatement.setOperationObject(o);
	}
	
	public void addUpdate(){
		daoStatement = new DaoStatement(SqlType.UPDATE, en);
		daoStatement.addItem(UpdateItem.newInstance(o));
		daoStatement.setOperationObject(o);
	}
	
	public void addDelete(){
		daoStatement = new DaoStatement(SqlType.BATCH, en);
		daoStatement.addItem(DeleteItem.newInstance(o));
		daoStatement.setOperationObject(o);
	}
	
	public void addBatchDelete(){
		addDelete();
	}
	
	public void addBatchUpdate(){
		daoStatement = new DaoStatement(SqlType.BATCH, en);
		daoStatement.addItem(UpdateItem.newInstance(o));
		daoStatement.setOperationObject(o);
	}
	
	public void addCreate(){
		daoStatement = new DaoStatement(SqlType.CREATE, en);
		daoStatement.addItem(CreateItem.newInstance());
	}
	
	public void addCreateEntity(){
		daoStatement = new DaoStatement(SqlType.CREATEENTITY, en);
		daoStatement.addItem(CreateEntityItem.newInstance());
		daoStatement.setRtnType(Map.class);
	}
	
	public void addDrop(){
		daoStatement = new DaoStatement(SqlType.DROP, en);
		daoStatement.addItem(DropItem.newInstance());
	}
	
	public void addPage(){
		addSelect();
		daoStatement.addItem(pageCnd);
	}
	
	public void addQueryFrom(int startIndex, int size){
		addSelect();
		daoStatement.addItem(QueryFromItem.newInstance(startIndex, size));
	}
	
	public void addUpdateChain(){
		daoStatement = new DaoStatement(SqlType.UPDATE, en);
		daoStatement.addItem(chain);
		daoStatement.addItem(cnd.getSqlExpsGroup());
	}
	
	/**
	 * @description
	 * 
	 * @param queryName
	 * @param objs
	 * @param clazz 返回List中的对象类型
	 * @return void
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	public void addQueryName(String queryName, Object[] objs, Class<?> clazz){
		daoStatement = new DaoStatement(SqlType.SELECT, en);
		daoStatement.addItem(QueryNameItem.newInstance(queryName, objs));
		if(cnd != null){
			daoStatement.addItem(cnd.getSqlExpsGroup());
			daoStatement.addItem(cnd.getOrderBySet());
		}
		daoStatement.setRtnType(clazz);
	}
	
	public void addQuerySql(String sql, Object[] objs, Class<?> clazz){
		daoStatement = new DaoStatement(SqlType.SELECT, en);
		daoStatement.addItem(QuerySqlItem.newInstance(sql, objs));
		daoStatement.setRtnType(clazz);
	}
	
	public void addExeSql(String sql, Object[] objs){
		daoStatement = new DaoStatement(SqlType.UPDATE, en);
		daoStatement.addItem(ExeSqlItem.newInstance(sql, objs));
	}
	
	public void setPageCnd(PageCnd pageCnd) {
		this.pageCnd = pageCnd;
	}
	
	public List<T> getList(){
		return daoStatement.getList(en.getType());
	}
	
	public <V> List<V> getList(Class<V> clazz){
		return daoStatement.getList(clazz);
	}
	
	public Object getObject(){
		Object object = daoStatement.getObject();
		if(object instanceof List){
			List<Object> list = (List<Object>) object;
			if(list.size() > 0) return list.get(0);
			return null;
		}
		return daoStatement.getObject();
	}
	
}
