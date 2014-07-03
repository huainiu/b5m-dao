package com.b5m.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.b5m.dao.Dao;
import com.b5m.dao.Trans;
import com.b5m.dao.domain.cnd.Cnd;
import com.b5m.dao.domain.cnd.Op;
import com.b5m.dao.domain.item.Chain;
import com.b5m.dao.domain.page.PageCnd;
import com.b5m.dao.domain.page.PageView;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DaoTest extends TestCase{
	private Dao dao;
	
	@Override
	protected void setUp() throws Exception {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://172.16.11.207:3306/b5m_adrs?autoReconnect=true&useUnicode=true&characterEncoding=UTF8&mysqlEncoding=utf8&zeroDateTimeBehavior=convertToNull");
		dataSource.setMinPoolSize(5);
		dataSource.setMaxPoolSize(20);
		dataSource.setMaxIdleTime(1800);
		dataSource.setInitialPoolSize(5);
		dataSource.setIdleConnectionTestPeriod(1200);
		dataSource.setUser("b5m");
		dataSource.setPassword("izene123");
		dao = new DaoImpl(dataSource);
		super.setUp();
	}
	
	public void testCreateEntity(){
		dao.createEntity("t_ad", "com.b5m.adrs.domain", "/home/echo/project/b5m/adrs/src/main/java/com/b5m/adrs/domain");
		dao.createEntity("t_ad_ower_manager", "com.b5m.adrs.domain", "/home/echo/project/b5m/adrs/src/main/java/com/b5m/adrs/domain");
		dao.createEntity("t_ad_report", "com.b5m.adrs.domain", "/home/echo/project/b5m/adrs/src/main/java/com/b5m/adrs/domain");
		dao.createEntity("t_user", "com.b5m.adrs.domain", "/home/echo/project/b5m/adrs/src/main/java/com/b5m/adrs/domain");
	}
	
	public void testSelectBySql(){
		List<Map> mapList = dao.queryBySql("select * from comments", null, Map.class);
		for(Map map : mapList){
			System.out.println(map);
		}
	}
	
	public void testDrop(){
		dao.drop(Person.class);
	}	
	
	public void testCreate(){
	}
	
	public void testUpdateChain(){
		Chain chain = Chain._new("name", "zhangsan1");
		Cnd cnd = Cnd.where("id", Op.EQ, 1);
		dao.update(Person.class, chain, cnd);
	}
	
	public void testInsert(){
		Person person = new Person();
		person.setName("zhangsan");
		person.setAge(31);
		person.setCode("11111");
		System.out.println(dao.insert(person));
	}
	
	public void testExeSql(){
		dao.exeSql("update t_person set name = ? where id = ?", new Object[]{"lisi", 1L});
	}
	
	public void testGet(){
		System.out.println(dao.get(Person.class, 1L));
	}
	
	public void testUpdate(){
		Person person = new Person();
		person.setId(1L);
		person.setName("zxx4");
		person.setAge(33);
		dao.update(person);
	}
	
	public void testBatchInsert(){
		List<Person> persons = new ArrayList<Person>(); 
		for(int i = 0; i < 100; i++){
			Person person = new Person();
			person.setName("zhangsan" + i);
			person.setAge(31 + i);
			person.setCode("11111" + i);
			persons.add(person);
		}
		dao.batchInsert(persons);
	}
	
	public void testBatchUpdate(){
		List<Person> persons = dao.query(Person.class, Cnd.where("id", Op.LT, "450"));
		int length = persons.size();
		for(int i = 0; i < length; i++){
			Person person = persons.get(i);
			person.setName(person.getName() + "--");
		}
		dao.batchUpdate(persons);
	}
	
	public void testNameQuery1(){
		System.out.println(dao.query(Person.class, "selectId", new Object[]{"2"}, Long.class));
	}
	
	public void testNameQuery(){
		System.out.println(dao.query(Person.class, "selectId1", Cnd.where("id", Op.EQ, 1l), Long.class));
	}
	
	public void testQuery(){
		List<Person> persons = dao.query(Person.class, Cnd.where("id", Op.LT, 10l));
		System.out.println(persons);
	}
	
	public void testQuery1(){
        System.out.println("start");
        List<Person> persons = dao.query(Person.class, Cnd._new().desc("name"));
        System.out.println(persons);
    }
	
	public void testQueryFrom(){
		System.out.println(dao.query(Person.class, 0, 10, Cnd._new()));
	}
	
	public void testQuerySql(){
		System.out.println(dao.queryBySql("select age from t_person where id = ?", new Object[]{"3"}, Integer.class));
	}
	
	public void testQueryPage(){
		PageView<Person> pageView = dao.queryPage(Person.class, Cnd._new(), PageCnd.newInstance(1, 10));
		System.out.println(pageView.getRecords());
		System.out.println(pageView.getTotalPage());
	}
	
	public void testBatchDelete(){
		List<Person> persons = dao.query(Person.class, Cnd.where("id", Op.GT, 50L));
		List<Long> ids = new ArrayList<Long>();
		for(long i = 1l; i <=50l; i++){
			ids.add(i);
		}
		dao.batchDelete(persons);
		dao.batchDelete(Person.class, ids);
	}
	
	public void testQueryComment(){
//		List<Comment> comments = dao.query(Comment.class, Cnd.where("content", Op.LIKE, "不错"));
//		List<Comment> comments = dao.query(Comment.class, Cnd.where("id", Op.BETWEEN, new Long[]{1L, 20L}));
//		List<Comment> comments = dao.query(Comment.class, Cnd.where("id", Op.IN, new Long[]{1L, 20L}));
		List<Person> persons = dao.query(Person.class, Cnd.where("id", Op.ISNOTNULL, null));
		for(Person person : persons){
			System.out.println(person.getId());
		}
		System.out.println(persons.size());
	}
	
	public void testTrans(){
		final Person person1 = dao.get(Person.class, 1L);
		final Person person2 = dao.get(Person.class, 2L);
		System.out.println(person1);
		System.out.println(person2);
		
		if(person1 != null) person1.setName(person1.getName() + "_");
		if(person2 != null) person2.setName(person2.getName() + "_");
		
		Trans._exe(new Runnable() {
			
			@Override
			public void run() {
				if(person1 != null) dao.update(person1);
				if(person2 != null) dao.update(person2);
			}
		});
	}
	
	public void testQueryCount1(){
		//System.out.println(dao.queryCount(Comment.class, Cnd._new()));
	}
	
	public void queryAll(){
		List<Person> persons = dao.queryAll(Person.class);
		System.out.println(persons);
	}
	
}
