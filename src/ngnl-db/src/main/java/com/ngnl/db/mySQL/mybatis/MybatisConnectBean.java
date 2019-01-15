package com.ngnl.db.mySQL.mybatis;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ngnl.core.annotations.NonNull;
import com.ngnl.core.utils.Assert;
import com.ngnl.db.ConnectBean;

/**
 * @author 47
 *
 */
public class MybatisConnectBean extends ConnectBean {

	SqlSessionFactory sqlSessionFactory = null;
	
	public MybatisConnectBean (@NonNull SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
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
	 * MySQLConnectDBBean's builder
	 */
	public static class Builder {
		
		/**
		 * TODO:
		 * use code style to create a {@code SqlSessionFactory}.
		 * @return
		 */
		public SqlSessionFactory build () {
			
			return null;
		}
		
		/**
		 * Create a {@code SqlSessionFactory} with a XML file.
		 * Use example:<br>
		 * <i>String resource = "org/mybatis/example/mybatis-config.xml";</i><br>
		 * <i>InputStream inputStream = Resources.getResourceAsStream(resource);</i><br>
		 * <i>new Builder().build(inputStream);</i>
		 * @param inputStream
		 */
		public static SqlSessionFactory build (InputStream inputStream) {
			return new SqlSessionFactoryBuilder().build(inputStream);
		}
	}
}
