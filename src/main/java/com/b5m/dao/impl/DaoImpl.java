package com.b5m.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.Dao;
import com.b5m.dao.DaoExecutor;
import com.b5m.dao.DaoRunner;
import com.b5m.dao.domain.cnd.Cnd;
import com.b5m.dao.domain.cnd.Op;
import com.b5m.dao.domain.item.Chain;
import com.b5m.dao.domain.page.PageCnd;
import com.b5m.dao.domain.page.PageView;
import com.b5m.dao.domain.page.QueryResult;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.utils.EntityOperation;
/**
 * @Company B5M.com
 * @description
 * 数据层访问
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DaoImpl extends DaoSupport implements Dao{
	
	public DaoImpl(DataSource dataSource){
		setDataSource(dataSource);
		runner = new DaoRunnerImpl();
		executor = new DaoExecutorImpl();
	}
	
	public DaoImpl(DataSource dataSource, DaoRunner runner, DaoExecutor executor){
		setDataSource(dataSource);
		setExecutor(executor);
		setRunner(runner);
	}

	@Override
	public <T> T insert(T t) {
		Entity<T> entity = this.holder.getEntityBy(t);
		EntityOperation<T> operation = newOperation(entity, t);
		operation.addInsert();
		operation.exec();
		return t;
	}

	@Override
	public <T> T update(T t) {
		Entity<T> entity = this.holder.getEntityBy(t);
		EntityOperation<T> operation = newOperation(entity, t);
		operation.addUpdate();
		operation.exec();
		return t;
	}
	
	@Override
	public <T> void delete(T t){
		Entity<T> entity = this.holder.getEntityBy(t);
		EntityOperation<T> operation = newOperation(entity, t);
		operation.addDelete();
		operation.exec();
	}
	
	@Override
	public <T> T get(Class<T> clazz, Long id) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityField entityField = entity.getIdField();
		EntityOperation<T> operation = newOperation(entity, Cnd.where(entityField.getName(), Op.EQ, id));
		operation.addSelect();
		operation.exec();
		return (T) operation.getObject();
	}
	
	@Override
	public <T> T getByPrimaryKey(Class<T> clazz, Serializable key) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityField entityField = entity.getIdField();
		if(entityField == null){
			entityField = entity.getNameField();
		}
		EntityOperation<T> operation = newOperation(entity, Cnd.where(entityField.getName(), Op.EQ, key));
		operation.addSelect();
		operation.exec();
		return (T) operation.getObject();
	}
	
	@Override
	public <T> T getByName(Class<T> clazz, String name) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityField entityField = entity.getNameField();
		EntityOperation<T> operation = newOperation(entity, Cnd.where(entityField.getName(), Op.EQ, name));
		operation.addSelect();
		operation.exec();
		return (T) operation.getObject();
	}
	
	@Override
	public synchronized <T> Entity<T> create(Class<T> clazz, boolean dropIfExists){
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, null);
		if(dropIfExists){
			drop(clazz);
		}
		operation.addCreate();
		operation.exec();
		return entity;
	}
	
	@Override
	public void createEntity(String tableName, String pkg, OutputStream out){
		Entity<?> entity = new Entity(null, tableName);
		EntityOperation operation = newOperation(entity, null);
		operation.addCreateEntity();
		operation.exec();
		Map<String, Integer> fields = (Map<String, Integer>) operation.getObject();
		createEntity(fields, tableName, pkg, out);
	}
	
	@Override
	public void createEntity(String tableName, String pkg, String dir){
		String entityName = getEntityName(tableName);
		File dirFile = new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(dir + "/" + entityName + ".java");
			createEntity(tableName, pkg, fileOutputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized <T> Entity<T> drop(Class<T> clazz){
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, null);
		operation.addDrop();
		operation.exec();
		return entity;
	}

	@Override
	public <T> List<T> query(Class<T> clazz, Cnd cnd) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, cnd);
		operation.addSelect();
		operation.exec();
		return operation.getList();
	}
	
	@Override
	public <T> T queryUnique(Class<T> clazz, Cnd cnd) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, cnd);
		operation.addSelect();
		operation.exec();
		return rtnUnique(operation.getList());
	}
	
	@Override
	public <T> PageView<T> queryPageBySql(String sql, Object[] params, PageCnd pageCnd, Class<T> rtnType){
		PageView<T> pageView = new PageView<T>(pageCnd.getPageSize(), pageCnd.getCurrentPage());
		Long count = queryUniqueBySql("select count(*) from (" + sql + ") o ", params, Long.class);
		if(params == null) params = new Object[]{};
		int length = params.length;
		params = Arrays.copyOf(params, params.length + 2);
		params[length] = (pageCnd.getCurrentPage() - 1) * pageCnd.getPageSize();
		params[length + 1] = pageCnd.getPageSize();
		List<T> list = queryBySql(sql + " limit ?,? ", params, rtnType);
		pageView.setTotalRecord(count);
		pageView.setRecords(list);
		return pageView;
	}

	@Override
	public <T> PageView<T> queryPage(Class<T> clazz, Cnd cnd, PageCnd pageCnd){
		int totalRecord = queryCount(clazz, cnd);
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, cnd);
		operation.setPageCnd(pageCnd);
		operation.addPage();
		operation.exec();
		List<T> list = operation.getList();
		PageView<T> pageView = new PageView<T>(pageCnd.getPageSize(), pageCnd.getCurrentPage());
		pageView.setQueryResult(QueryResult.newInstance(list, totalRecord));
		return pageView;
	}
	
	@Override
	public <T> List<T> query(Class<T> clazz, int startIndex, int size, Cnd cnd){
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, cnd);
		operation.addQueryFrom(startIndex, size);
		operation.exec();
		return operation.getList();
	}
	
	@Override
	public <T> int queryCount(Class<T> clazz, Cnd cnd) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, cnd);
		operation.addSelectCount();
		operation.exec();
		return (Integer) operation.getObject();
	}

	@Override
	public <T> List<T> queryAll(Class<T> clazz) {
		Entity<T> entity = this.holder.getEntity(clazz);
		EntityOperation<T> operation = newOperation(entity, Cnd._new());
		operation.addSelect();
		operation.exec();
		return operation.getList();
	}
	
	@Override
    public <T> void batchInsert(List<T> ts){
    	if(ts.size() < 1) return;
    	Entity<T> entity = this.holder.getEntityBy(ts.get(0));
    	EntityOperation<T> operation = newOperation(entity, ts);
		operation.addBatchInsert();
		operation.exec();
    }
	
    @Override
    public <T> void batchUpdate(List<T> ts){
    	if(ts.size() < 1) return;
    	Entity<T> entity = this.holder.getEntityBy(ts.get(0));
    	EntityOperation<T> operation = newOperation(entity, ts);
    	operation.addBatchUpdate();
    	operation.exec();
    }
    
    @Override
    public <T> void batchDelete(List<T> ts){
    	if(ts.size() < 1) return;
    	Entity<T> entity = this.holder.getEntityBy(ts.get(0));
    	EntityOperation<T> operation = newOperation(entity, ts);
    	operation.addBatchDelete();
    	operation.exec();
    }
    
    @Override
    public <T> void batchDelete(Class<T> clazz, List<Long> ids){
    	if(ids.size() < 1) return;
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, ids);
    	operation.addBatchDelete();
    	operation.exec();
    }
    
    @Override
    public <T> List<T> query(Class<T> clazz, String queryName, Object[] objs){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, null);
    	operation.addQueryName(queryName, objs, clazz);
    	operation.exec();
    	return operation.getList();
    }
    
    @Override
    public <T> T queryUnique(Class<T> clazz, String queryName, Object[] objs){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, null);
    	operation.addQueryName(queryName, objs, clazz);
    	operation.exec();
    	return rtnUnique(operation.getList());
    } 
    
    @Override
    public <T> List<T> query(Class<T> clazz, String queryName, Cnd cnd){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, cnd);
    	operation.addQueryName(queryName, null, clazz);
    	operation.exec();
    	return operation.getList();
    } 
    
    @Override
    public <T> T queryUnique(Class<T> clazz, String queryName, Cnd cnd){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, cnd);
    	operation.addQueryName(queryName, null, clazz);
    	operation.exec();
    	return rtnUnique(operation.getList());
    }
    
    @Override
    public <T> List<T> queryBySql(String sql, Object[] objs, Class<T> rtnType){
    	EntityOperation<T> operation = newOperation(null, null);
    	operation.addQuerySql(sql, objs, rtnType);
    	operation.exec();
    	return operation.getList(rtnType);
    }
    
    @Override
    public <T> T queryUniqueBySql(String sql, Object[] objs, Class<T> rtnType){
    	EntityOperation<T> operation = newOperation(null, null);
    	operation.addQuerySql(sql, objs, rtnType);
    	operation.exec();
    	return rtnUnique(operation.getList(rtnType));
    }
    
    public void exeSql(String sql, Object[] objs){
    	EntityOperation operation = newOperation(null, null);
    	operation.addExeSql(sql, objs);
    	operation.exec();
	}
    
    @Override
    public <T, V> List<V> query(Class<T> clazz, String queryName, Cnd cnd, Class<V> rtnType){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, cnd);
    	if(rtnType == null){
    		operation.addQueryName(queryName, null, clazz);
    	}else{
    		operation.addQueryName(queryName, null, rtnType);
    	}
    	operation.exec();
    	return operation.getList(rtnType);
    } 
    
    @Override
    public <T, V> V queryUnique(Class<T> clazz, String queryName, Cnd cnd, Class<V> rtnType){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, cnd);
    	if(rtnType == null){
    		operation.addQueryName(queryName, null, clazz);
    	}else{
    		operation.addQueryName(queryName, null, rtnType);
    	}
    	operation.exec();
    	return rtnUnique(operation.getList(rtnType));
    }
    
    @Override
    public <T, V> List<V> query(Class<T> clazz, String queryName, Object[] objs, Class<V> rtnType){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, null);
    	if(rtnType == null){
    		operation.addQueryName(queryName, objs, clazz);
    	}else{
    		operation.addQueryName(queryName, objs, rtnType);
    	}
    	operation.exec();
    	return operation.getList(rtnType);
    }
    
    @Override
    public <T, V> V queryUnique(Class<T> clazz, String queryName, Object[] objs, Class<V> rtnType){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, null);
    	if(rtnType == null){
    		operation.addQueryName(queryName, objs, clazz);
    	}else{
    		operation.addQueryName(queryName, objs, rtnType);
    	}
    	operation.exec();
    	return rtnUnique(operation.getList(rtnType));
    }
    
    public <T> void update(Class<T> clazz, Chain chain, Cnd cnd){
    	Entity<T> entity = this.holder.getEntity(clazz);
    	EntityOperation<T> operation = newOperation(entity, chain, cnd);
    	operation.addUpdateChain();
    	operation.exec();
    }
    
	protected <T> EntityOperation<T> newOperation(Entity<T> entity, Object o){
		return new EntityOperation(this, entity, o);
	}
	
	protected <T> EntityOperation<T> newOperation(Entity<T> entity, Cnd cnd){
		return new EntityOperation(this, entity, cnd);
	}
	
	protected <T> EntityOperation<T> newOperation(Entity<T> entity, Chain chain, Cnd cnd){
		return new EntityOperation(this, entity, chain, cnd);
	}
	
	protected void createEntity(Map<String, Integer> fields, String tableName, String pkg, OutputStream out){
		StringBuilder sb = new StringBuilder();
		StringBuilder impt = new StringBuilder();
		StringBuilder entity = new StringBuilder();
		StringBuilder setAndGet = new StringBuilder();
		sb.append("package  ").append(pkg).append(";");
		sb.append("\r\n");
		sb.append("");
		
		impt.append("import com.b5m.dao.annotation.Column;");
		impt.append("\r\n");
		impt.append("import com.b5m.dao.annotation.Table;");
		impt.append("\r\n");
		impt.append("import com.b5m.dao.domain.ColType;");
		impt.append("\r\n");
		
		entity.append("\r\n");
		entity.append("\r\n");
		entity.append("@Table(\"").append(tableName).append("\")");
		entity.append("\r\n");
		entity.append("public class " + getEntityName(tableName) + " {");
		entity.append("\r\n");
		entity.append("\r\n");
		List<Integer> haveImptTypes = new ArrayList<Integer>();
		for(String field : fields.keySet()){
			Integer type = fields.get(field);
			String fieldName = firstLow(getEntityName(field));
			setType(impt, type, haveImptTypes);
			if("id".equals(field)){
				entity.append("  @Id");
			}else{
				entity.append("  @Column(name = \"").append(field).append("\")");
			}
			entity.append("\r\n");
			entity.append("  private ").append(getType(type)).append(" ").append(fieldName).append(";");
			entity.append("\r\n");
			entity.append("\r\n");
			
			setAndGet.append("  public void ").append(getSetFieldName(field)).append("(").append(getType(type)).append(" ").append(fieldName).append(") {");
			setAndGet.append("\r\n");
			setAndGet.append("      this.").append(fieldName).append(" = ").append(fieldName).append(";");
			setAndGet.append("\r\n");
			setAndGet.append("  }");
			setAndGet.append("\r\n");
			setAndGet.append("\r\n");
			
			setAndGet.append("  public ").append(getType(type)).append(" ").append(getGetFieldName(field)).append("()").append(" {");
			setAndGet.append("\r\n");
			setAndGet.append("     return ").append(fieldName).append(";");
			setAndGet.append("\r\n");
			setAndGet.append("  }");
			setAndGet.append("\r\n");
			setAndGet.append("\r\n");
		}
		
		sb.append(impt).append(entity).append(setAndGet).append("}");
		try {
			out.write(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setType(StringBuilder impt, Integer type, List<Integer> haveImptTypes) {
		if(haveImptTypes.contains(type)){
			return;
		}
		haveImptTypes.add(type);
		if(isDate(type)){
			impt.append("\r\n");
			impt.append("import java.util.Date;");
		}
		if(java.sql.Types.DOUBLE == type || java.sql.Types.DECIMAL == type || java.sql.Types.FLOAT == type){
			impt.append("\r\n");
			impt.append("import java.math.BigDecimal;");
		}
	}

	protected String getType(Integer type){
		if(java.sql.Types.INTEGER == type){
			return "Integer";
		}
		if(java.sql.Types.BIGINT == type){
			return "Long";
		}
		if(java.sql.Types.BIT == type){
			return "boolean";
		}
		if(isDate(type)){
			return "Date";
		}
		if(java.sql.Types.DOUBLE == type || java.sql.Types.DECIMAL == type || java.sql.Types.FLOAT == type){
			return "BigDecimal";
		}
		return "String";
	}
	
	protected boolean isDate(Integer type){
		if(java.sql.Types.DATE == type || java.sql.Types.TIME == type || java.sql.Types.TIMESTAMP == type){
			return true;
		}
		return false;
	}
	
	protected String getSetFieldName(String fieldName){
		return "set" + getEntityName(fieldName);
	}
	
	protected String getGetFieldName(String fieldName){
		return "get" + getEntityName(fieldName);
	}
	
	protected String getEntityName(String tableName){
		if(tableName.startsWith("t_")){
			tableName = tableName.substring(2);
		}
		if(tableName.indexOf("_") > 0){
			String[] prexs = tableName.split("_");
			String newTableName = "";
			for(String prex : prexs){
				newTableName += firstUp(prex);
			}
			return newTableName;
		}
		return firstUp(tableName);
	}
	
	protected String firstUp(String str){
		String s = str.substring(0, 1);
		return StringUtils.upperCase(s) + str.substring(1, str.length());
	}
	
	protected String firstLow(String str){
		String s = str.substring(0, 1);
		return StringUtils.lowerCase(s) + str.substring(1, str.length());
	}
	
	protected <T> T rtnUnique(List<T> list){
    	if(list.isEmpty()) return null;
    	return list.get(0);
    }

}
