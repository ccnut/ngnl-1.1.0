package com.ngnl.db.test.mybatis;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.ngnl.db.mySQL.mybatis.MyBatisConnector;
import com.ngnl.db.mySQL.mybatis.MybatisConnectBean;

public class MybatisCommectorTest {
	
	Logger logger = LoggerFactory.getLogger(MybatisCommectorTest.class);
	
	public static MyBatisConnector myBatisConnector = null;

	@BeforeClass
	public static void beforeClass () throws Exception{
		InputStream inputStream = Resources.getResourceAsStream("com/ngnl/db/test/mybatis/config/dbConfig.xml");
		MybatisConnectBean connectBean = MybatisConnectBean.newBuilder().loadXML(inputStream)
									   									.build();
		myBatisConnector = new MyBatisConnector(connectBean);
	}
	
	@Test
	public void getSqlSessionFactoryTest () {
		SqlSessionFactory sqlSessionFactory = myBatisConnector.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		User user = userDao.findUserById(1);
		logger.info(JSON.toJSONString(user));
	}
	
}
