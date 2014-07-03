package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class CreateItem implements SqlItem{
	
	public CreateItem(){}
	
	public static CreateItem newInstance(){
		return new CreateItem();
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("CREATE TABLE IF NOT EXISTS ").append(en.getTableName()).append("(");
		List<EntityField> fields = en.getFields();
		for(EntityField field : fields){
			sb.append(field.getColumnName());
			sb.append(" ").append(evalFieldType(field));
			if(isInteger(field.getTypeClass()) && field.isUnsigned()) sb.append(" UNSIGNED");
			if(field.isNotNull()) sb.append(" NOT NULL");
			if(field.isAuto()) sb.append(" AUTO_INCREMENT");
			sb.append(",");
		}
		EntityField primaryField = en.getIdField();
		if(primaryField == null) primaryField = en.getNameField();
		if(primaryField != null){
			sb.append("primary key(" + primaryField.getColumnName() + ")");
		}
		sb.append(")ENGINE=InnoDB CHARSET=utf8");
	}
	
	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		//没有参数操作
	}
	
	private boolean isInteger(Class<?> type){
		return Integer.class.equals(type) || int.class.equals(type);
	}
	
	private String evalFieldType(EntityField field){
		int length = field.getLength();
		switch (field.getColType()) {
		case INT:
			if (length <= 0)
				return "INT(32)";
			else if (length <= 4) {
				return "TINYINT(" + (length * 4) + ")";
			} else if (length <= 8) {
				return "INT(" + (length * 4) + ")";
			}
			return "BIGINT(" + (length * 4) + ")";
		case CHAR:
			if(length <= 0) length = 10;
			return "CHAR(" + length + ")";	
		case BOOLEAN:
			return "BOOLEAN";
		case VARCHAR:
			if(length <= 0) length = 255;
			return "VARCHAR(" + length + ")";
		case TEXT:
			return "TEXT";
		case TIMESTAMP:
			return "TIMESTAMP";
		case DATETIME:
			return "DATETIME";
		case DATE:
			return "DATE";
		case TIME:
			return "TIME";
		case LONG:
			if(length <= 0) length = 10;
			return "BIGINT(" + (length * 4) + ")";
		case FLOAT:
			if (length > 0 && field.getPrecision() > 0) {
				return "NUMERIC(" + length + "," + field.getPrecision() + ")";
			}
			return "FLOAT";
		case DOUBLE:
			// 用默认精度
			return "NUMERIC(15,10)";
		default:
			break;
		}
		return "VARCHAR(255)";
	}
}
