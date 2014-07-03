package com.b5m.dao.domain.item;

import java.util.ArrayList;
import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.LogUtils;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-11
 * @email echo.weng@b5m.com
 */
@SuppressWarnings("unchecked")
public class DeleteItem implements SqlItem{
	private Object o;
	
	public DeleteItem(Object o){
		this.o = o;
	}
	
	public static DeleteItem newInstance(Object o){
		return new DeleteItem(o);
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("DELETE FROM").append(" ").append(en.getTableName()).append(" ").append("WHERE").append(" ");
		EntityField primaryField = primaryField(en);
		sb.append(primaryField.getColumnName()).append(" = ?");
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		EntityField primaryField = primaryField(en);
		//o 有四种情况 List<id>,id List<Entity>, Entity
		if(o instanceof List){//List<id> List<Entity>
			List<Object> os = (List<Object>) o;
			int length = os.size();
			if(length > 0){
				if(isEntity(os.get(0), en)){
					try {
						for(Object object : os){
							Object value = primaryField.getReadMethod().invoke(object, null);
							List<Object> oneParams = new ArrayList<Object>(1);
							oneParams.add(value);
							params.add(oneParams);
						}
					} catch (Exception e) {
						LogUtils.error(this.getClass(), e);
						throw new DaoException(e);
					}
				}else{
					for(Object object : os){
						List<Object> oneParams = new ArrayList<Object>();
						oneParams.add(object);
						params.add(oneParams);
					}
				}
			}
		}else{
			if(en.getType().equals(o.getClass())){
				try {
					Object value = primaryField.getReadMethod().invoke(o, null);
					params.add(value);
				} catch (Exception e) {
					LogUtils.error(this.getClass(), e);
					throw new DaoException(e);
				}
			}else{
				params.add(o);
			}
		}
		fieldTyps.add(primaryField.getTypeClass());
		colTypes.add(primaryField.getColType());
	}

	private boolean isEntity(Object o, Entity<?> en){
		return o.getClass().equals(en.getType());
	}
	
	private EntityField primaryField(Entity<?> en){
		EntityField primaryField = en.getIdField();
		if(primaryField == null){
			primaryField = en.getNameField();
		}
		return primaryField;
	}
}
