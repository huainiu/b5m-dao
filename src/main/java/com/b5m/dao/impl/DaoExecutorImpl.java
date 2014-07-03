package com.b5m.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.DaoExecutor;
import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.DaoStatement;
import com.b5m.dao.domain.SqlType;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.Daos;
import com.b5m.dao.utils.LogUtils;
/**
 * @Company B5M.com
 * @description
 * sql语句执行
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
@SuppressWarnings("unchecked")
public class DaoExecutorImpl implements DaoExecutor {
	public void exec(Connection conn, DaoStatement st) {
		try {
			switch (st.getSqlType()) {
				case SELECTCOUNT:
				case CREATEENTITY:
				case SELECT:
					select(conn, st);
					break;
				case UPDATE:
				case DELETE:
				case ADD:
				case CREATE:
				case DROP:
				case BATCH:
					_exec(conn, st, st.getSqlType());
					break;
				default:
			}
		} catch (SQLException e) {
			LogUtils.error(this.getClass(), e);
			throw new DaoException(e);
		}
	}
	
	private void _exec(Connection conn, DaoStatement st, SqlType sqlType) throws SQLException{
		String sql = st.toSql();
		LogUtils.info(this.getClass(), "execute sql is : [" + sql + "], and params is [" + st.getParams() + "] for sql type is [" + sqlType + "]");
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		boolean isbatch = setValues(preparedStatement, st.getParams(), st.getFieldTypes(), st.getColTypes());
		if(isbatch){
			preparedStatement.executeBatch();
		}else{
			preparedStatement.executeUpdate();
		}
		if(SqlType.ADD.equals(sqlType) && st.isPrimaryAuto()){
			ResultSet rs = preparedStatement.getGeneratedKeys();
			st.onAfter(conn, rs);
			Daos.safeClose(rs);
		}
		Daos.safeClose(preparedStatement);
	}
	
	private void select(Connection conn, DaoStatement st) throws SQLException{
		ResultSet resultSet = null;
		String sql = st.toSql();
		LogUtils.info(this.getClass(), "execute sql is : [" + sql + "], and params is [" + st.getParams() + "] for sql type is [" + SqlType.SELECT + "]");
		List<Object> params = st.getParams();
		if(params.size() < 1){
			Statement statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			st.onAfter(conn, resultSet);
			Daos.safeClose(statement);
		}else{
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			setValues(preparedStatement, params, st.getFieldTypes(), st.getColTypes());
			resultSet = preparedStatement.executeQuery();
			st.onAfter(conn, resultSet);
			Daos.safeClose(preparedStatement);
		}
		Daos.safeClose(resultSet);
	}
	
	/**
	 * 返回是否是批量
	 * @param preparedStatement
	 * @param params
	 * @param fieldTypes
	 * @param colTypes
	 * @return
	 * @throws SQLException
	 */
	private boolean setValues(PreparedStatement preparedStatement, List<Object> params, List<Class<?>> fieldTypes, List<ColType> colTypes) throws SQLException{
		int length = params.size();
		if(length < 1) return false;
		boolean paramType = true;
		if(fieldTypes.isEmpty()){
			paramType = false;
		}
		if(params.get(0) instanceof List){
			for(Object object : params){
				List<Object> os = (List<Object>) object;
				length = os.size();
				for(int i = 0; i < length; i++){
					if(!paramType){
						setValue(preparedStatement, os.get(i), null, null, i + 1);
					}else{
						setValue(preparedStatement, os.get(i), fieldTypes.get(i), colTypes.get(i), i + 1);
					}
				}
				preparedStatement.addBatch();
			}
			return true;
		}else{
			for(int i = 0; i < length; i++){
				if(paramType){
					setValue(preparedStatement, params.get(i), fieldTypes.get(i), colTypes.get(i), i + 1);
				}else{
					setValue(preparedStatement, params.get(i), null, null, i + 1);
				}
			}
		}
		return false;
	}
	
	private void setValue(PreparedStatement preparedStatement, Object param, Class<?> fieldType, ColType colType, int index) throws SQLException{
		if(fieldType == null){
			fieldType = param.getClass();
		}
		if(char.class.equals(fieldType) || Character.class.equals(fieldType)){
			setChar(preparedStatement, param, index);
		}else if(String.class.equals(fieldType)){
			setString(preparedStatement, param, index);
		}else if(Integer.class.equals(fieldType) || int.class.equals(fieldType)){
			setInt(preparedStatement, param, index);
		}else if(BigDecimal.class.equals(fieldType)){
			setBigDecimal(preparedStatement, param, index);
		}else if(Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)){
			setBoolean(preparedStatement, param, index);
		}else if(Long.class.equals(fieldType) || long.class.equals(fieldType)){
			setLong(preparedStatement, param, index);
		}else if(Byte.class.equals(fieldType) || byte.class.equals(fieldType)){
			setByte(preparedStatement, param, index);
		}else if(Short.class.equals(fieldType) || short.class.equals(fieldType)){
			setShort(preparedStatement, param, index);
		}else if(Float.class.equals(fieldType) || float.class.equals(fieldType)){
			setFloat(preparedStatement, param, index);
		}else if(Double.class.equals(fieldType) || double.class.equals(fieldType)){
			setDouble(preparedStatement, param, index);
		}else if(Date.class.equals(fieldType) || Timestamp.class.equals(fieldType) || Time.class.equals(fieldType) || java.sql.Date.class.equals(fieldType)){			
			setDate(preparedStatement, colType, param, index);
		}else if(Number.class.equals(fieldType)){
			setNumber(preparedStatement, colType, param, index);
		}else{
			setObject(preparedStatement, param, index);
		}
	}
	
	private void setObject(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param) {
			preparedStatement.setString(index, null);
		} else {
			String v = param.toString();
			preparedStatement.setString(index, v);
		}
	}
	
	private void setNumber(PreparedStatement preparedStatement, ColType colType, Object param, int index) throws SQLException{
		Number number = (Number)param;
		if(colType == null){
			if (null == param) {
				preparedStatement.setNull(index, Types.INTEGER);
			}else{
				preparedStatement.setInt(index, number.intValue());
			}
			return;
		}
		switch (colType) {
			case INT:
				if (null == param) { preparedStatement.setNull(index, Types.INTEGER);
				}else{
					preparedStatement.setInt(index, number.intValue());
				}
				break;
			case FLOAT:
				if (null == param) {
					preparedStatement.setNull(index, Types.FLOAT);
				}else{
					preparedStatement.setFloat(index, number.floatValue());
				}
				break;
			case LONG:
				if (null == param) {
					preparedStatement.setNull(index, Types.BIGINT);
				}else{
					preparedStatement.setLong(index, number.longValue());
				}
				break;
			case DOUBLE:
				if (null == param) {
					preparedStatement.setNull(index, Types.DOUBLE);
				}else{
					preparedStatement.setDouble(index, number.doubleValue());
				}
				break;
			default:
				if (null == param) {
					preparedStatement.setNull(index, Types.INTEGER);
				}else{
					preparedStatement.setInt(index, number.intValue());
				}
				break;
		}
	}
	
	private void setDate(PreparedStatement preparedStatement, ColType colType, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.TIMESTAMP);
		} else {
			if(param instanceof String){
				preparedStatement.setString(index, param.toString());
				return;
			}
			Date date = (Date)param;
			if(colType == null){
				preparedStatement.setTimestamp(index, new Timestamp(date.getTime()));
				return;
			}
			switch (colType) {
			case TIMESTAMP:
				preparedStatement.setTimestamp(index, new Timestamp(date.getTime()));
				break;
			case DATETIME:
			case DATE:
				preparedStatement.setDate(index, new java.sql.Date(date.getTime()));
				break;
			case TIME:
				preparedStatement.setTime(index, new Time(date.getTime()));
				break;
			default:
				break;
			}
		}
	}
	
	private void setDouble(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.INTEGER);
		} else {
			if(param instanceof String){
				param = Double.valueOf(param.toString());
			}
			preparedStatement.setDouble(index, (Double)param);
		}
	}
	
	private void setFloat(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.INTEGER);
		} else {
			if(param instanceof String){
				param = Float.valueOf(param.toString());
			}
			preparedStatement.setFloat(index, (Float)param);
		}
	}
	
	private void setShort(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.INTEGER);
		} else {
			if(param instanceof String){
				param = Short.valueOf(param.toString());
			}
			preparedStatement.setShort(index, (Short)param);
		}
	}
	
	private void setByte(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param) {
			preparedStatement.setNull(index, Types.TINYINT);
		} else {
			preparedStatement.setByte(index, (Byte)param);
		}
	}
	
	private void setLong(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.INTEGER);
		} else {
			if(param instanceof String){
				param = Long.valueOf(param.toString());
			}
			preparedStatement.setLong(index, Long.valueOf(param.toString()));
		}
	}
	
	private void setBoolean(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.BOOLEAN);
		} else {
			if(param instanceof String){
				param = Boolean.valueOf(param.toString());
			}
			preparedStatement.setBoolean(index, (Boolean)param);
		}
	}
	
	private void setBigDecimal(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.BIGINT);
		} else {
			if(param instanceof String){
				param = new BigDecimal(param.toString());
			}
			preparedStatement.setBigDecimal(index, (BigDecimal)param);
		}
	}
	
	private void setInt(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param || StringUtils.isEmpty(param.toString())) {
			preparedStatement.setNull(index, Types.INTEGER);
		} else {
			if(param instanceof String){
				param = Integer.valueOf(param.toString());
			}
			preparedStatement.setInt(index, (Integer)param);
		}
	}
	
	private void setString(PreparedStatement preparedStatement, Object param, int index)throws SQLException{
		if (null == param) {
			preparedStatement.setString(index, null);
		} else {
			preparedStatement.setString(index, param.toString());
		}
	}
	
	private void setChar(PreparedStatement preparedStatement, Object param, int index) throws SQLException{
		if (null == param) {
			preparedStatement.setString(index, null);
		} else {
			String s;
			if (param instanceof Character) {
				int c = ((Character) param).charValue();
				if (c >= 0 && c <= 32)
					s = " ";
				else
					s = String.valueOf((char) c);
			} else
				s = param.toString();
			preparedStatement.setString(index, s);
		}
	}
}
