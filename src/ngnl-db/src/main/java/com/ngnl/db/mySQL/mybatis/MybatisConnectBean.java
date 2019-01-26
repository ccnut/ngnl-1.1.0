package com.ngnl.db.mySQL.mybatis;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ngnl.core.utils.Assert;
import com.ngnl.db.ConnectBean;

/**
 * @author 47
 *
 */
public class MybatisConnectBean extends ConnectBean {

	SqlSessionFactory sqlSessionFactory = null;
	
	
	public static MybatisConnectBeanBuilder newBuilder () {
		return new MybatisConnectBean.MybatisConnectBeanBuilder();
	}

	/**
	 * @return the sqlSessionFactory
	 */
	public final SqlSessionFactory getSqlSessionFactory() {
		Assert.notNull(sqlSessionFactory, "sqlSessionFactory has not initialized.");
		return sqlSessionFactory;
	}
	
	
	/**
	 * @author 47
	 * MybatisConnectBeanBuilder builder
	 */
	public static class MybatisConnectBeanBuilder {
		
		SqlSessionFactory sqlSessionFactory = null;
		
		public MybatisConnectBean build () {
			MybatisConnectBean bean = new MybatisConnectBean();
			bean.sqlSessionFactory = sqlSessionFactory;
			return bean;
		}
		
		/**
		 * Create a {@code SqlSessionFactory} with a XML file.
		 * Use example:<br>
		 * <i>String resource = "org/mybatis/example/mybatis-config.xml";</i><br>
		 * <i>InputStream inputStream = Resources.getResourceAsStream(resource);</i><br>
		 * <i>loadXML(inputStream);</i>
		 * @param inputStream
		 */
		public MybatisConnectBeanBuilder loadXML (InputStream xmlInputStream) {
			this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(xmlInputStream);
			return this;
		}
	}
}
