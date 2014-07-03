package com.b5m.dao;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import com.b5m.dao.domain.DaoStatement;
import com.b5m.dao.domain.cnd.Cnd;
import com.b5m.dao.domain.item.Chain;
import com.b5m.dao.domain.page.PageCnd;
import com.b5m.dao.domain.page.PageView;
import com.b5m.dao.domain.table.Entity;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public interface Dao {
	
	/**
	 * 插入并返回对象 包括主键
	 * @description
	 *
	 * @param t
	 * @return
	 * @return T
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> T insert(T t);
	
	/**
	 * @description
	 * 数据更新
	 * @param t
	 * @return void
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> T update(T t);
	
	/**
	 * @param t
	 */
	<T> void delete(T t);
	
	/**
	 * @description
	 * 根据ID获取数据 系统中默认主键就用Long id
	 * @param id
	 * @return
	 * @return T
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> T get(Class<T> clazz, Long id);
	
	/**
	 * @description
	 * 根据主健获取数据
	 * @param id
	 * @return
	 * @return T
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> T getByPrimaryKey(Class<T> clazz, Serializable key);
	
	/**
	 * @description
	 * 根据Name获取数据 name作为主健
	 * @param id
	 * @return
	 * @return T
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> T getByName(Class<T> clazz, String name);
	
	/**
	 * @description
	 * 返回clazz这个类型的所有数据
	 * @return
	 * @return List<T>
	 * @date 2013-7-6
	 * @author xiuqing.weng
	 */
	<T> List<T> queryAll(Class<T> clazz);
	
	/**
	 * @param clazz
	 * @param dropIfExists
	 * @return
	 */
	<T> Entity<T> create(Class<T> clazz, boolean dropIfExists);
	
	/**
	 * @description
	 * 创建实体
	 * 不要在系统中使用
	 * @param tableName
	 */
	void createEntity(String tableName, String pkg, OutputStream out);
	
	/**
	 * @description
	 * 创建实体
	 * 不要在系统中使用
	 * @param tableName
	 * @param pkg 
	 * @param dir 输出的目录
	 */
	void createEntity(String tableName, String pkg, String dir);
	
	/**
	 * @param cnd
	 * @return
	 */
	<T> List<T> query(Class<T> clazz, Cnd cnd);
	
	/**
	 * @param cnd
	 * @return
	 */
	<T> T queryUnique(Class<T> clazz, Cnd cnd);
    
	/**
	 * @param clazz
	 * @return
	 */
	<T> Entity<T> drop(Class<T> clazz);
	
	/**
	 * @description
	 * 根据sql进行分页查询 rtnType 返回的类型
	 * @param sql
	 * @param params
	 * @param pageCnd
	 * @param rtnType
	 * @return
	 * @author echo weng
	 * @since 2013年12月26日
	 * @mail echo.weng@b5m.com
	 */
	<T> PageView<T> queryPageBySql(String sql, Object[] params, PageCnd pageCnd, Class<T> rtnType);
	
	/**
	 * 分页查询
	 * @param clazz
	 * @param cnd
	 * @param pageCnd
	 * @return
	 */
	<T> PageView<T> queryPage(Class<T> clazz, Cnd cnd, PageCnd pageCnd);
	
	/**
	 * @description
	 * 查询
	 * @param clazz
	 * @param startIndex 从第几个开始查询
	 * @param size 查询多少个
	 * @param cnd
	 * @return
	 */
	<T> List<T> query(Class<T> clazz, int startIndex, int size, Cnd cnd);
	
	/**
	 * @param clazz
	 * @param cnd
	 * @return
	 */
	<T> int queryCount(Class<T> clazz, Cnd cnd);
	
	int _exec(DaoStatement... sts);
	
	/**
	 * 批量插入
	 * @param ts
	 */
	<T> void batchInsert(List<T> ts);
	
	/**
	 * 批量更新
	 * @param ts
	 */
	<T> void batchUpdate(List<T> ts);
	
	/**
	 * 批量删除
	 * @param ts
	 */
	<T> void batchDelete(List<T> ts);
	
	/**
	 * @param clazz
	 * @param ids
	 */
	<T> void batchDelete(Class<T> clazz, List<Long> ids);
	
	/**
	 * @description
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param objs
	 * @return
	 * @return List<T>
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	<T> List<T> query(Class<T> clazz, String queryName, Object[] objs);
	
	/**
	 * @description
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param objs
	 * @return
	 * @return List<T>
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	<T> T queryUnique(Class<T> clazz, String queryName, Object[] objs);
	
	/**
	 * @description
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param cnd
	 * @return
	 */
	<T> List<T> query(Class<T> clazz, String queryName, Cnd cnd);
	
	/**
	 * @description
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param cnd
	 * @return
	 */
	<T> T queryUnique(Class<T> clazz, String queryName, Cnd cnd);
	
	/**
	 * @description
	 * query by sql
	 * @param sql
	 * @param objs 参数
	 * @param rtnType 需要返回的类型
	 * @return
	 */
	<T> List<T> queryBySql(String sql, Object[] objs, Class<T> rtnType);
	
	/**
	 * @description
	 * query by sql
	 * @param sql
	 * @param objs 参数
	 * @param rtnType 需要返回的类型
	 * @return
	 */
	<T> T queryUniqueBySql(String sql, Object[] objs, Class<T> rtnType);
	
	/**
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param cnd
	 * @param rtnType
	 * @return
	 */
	<T, V> List<V> query(Class<T> clazz, String queryName, Cnd cnd, Class<V> rtnType);
	
	/**
	 * 根据在实体头部 @NamedQueries 这个注解注入的sql语句名称进行查询
	 * @param clazz
	 * @param queryName
	 * @param cnd
	 * @param rtnType
	 * @return
	 */
	<T, V> V queryUnique(Class<T> clazz, String queryName, Cnd cnd, Class<V> rtnType);
	
	/**
     * 返回的是 rtnType 这个类型的list, 如果rtnType为空 则返回clazz的List
     * @param clazz 是实体 头部注入sql
     * @param queryName
     * @param objs
     * @param rtnType
     * @return
     */
	<T, V> List<V> query(Class<T> clazz, String queryName, Object[] objs, Class<V> rtnType);
	
	/**
     * 返回的是 rtnType 这个类型的list, 如果rtnType为空 则返回clazz的List
     * @param clazz 是实体 头部注入sql
     * @param queryName
     * @param objs
     * @param rtnType
     * @return
     */
	<T, V> V queryUnique(Class<T> clazz, String queryName, Object[] objs, Class<V> rtnType);
	
	/**
	 * @description
	 * 针对字段进行更新
	 * @param clazz
	 * @param chain
	 * @param cnd
	 */
	<T> void update(Class<T> clazz, Chain chain, Cnd cnd);
	
	/**
	 * @description
	 * 根据sql进行执行 objs 是参数
	 * @param sql
	 * @param objs
	 * @author echo
	 * @since 2013-7-30
	 * @email echo.weng@b5m.com
	 */
	void exeSql(String sql, Object[] objs);
}
