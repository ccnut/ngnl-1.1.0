package com.ngnl.db.mySQL.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ngnl.core.utils.Assert;

/**
 * @author 47
 *
 */
public class MyBatisConnector {
	
	MybatisConnectBean connectBean;

	public MyBatisConnector () {
	}
	
	public MyBatisConnector (MybatisConnectBean connectBean) {
		this.connectBean = connectBean;
	}
	
	/**
	 * Get a registered <i>Mapper</i>.<br>
	 * This method should always be surrended by try-finally block.To ensure that
	 * {@code SqlSession} is always be closed.<br>
	 * try {<br>
	 * Mapper mapper = getMapper(mapperClazz);<br>
	 * //<b>do somthing</b><br>
	 * }finally {<br>
	 * sqlSession.close();<br>
	 * }<br>
	 * 
	 * @param clazz
	 * @return
	 */
	public <M> M getMapper (Class<M> mapperClazz) {
		SqlSessionFactory sqlSessionFactory = this.connectBean.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		M mapper = null;
		try {
			mapper = sqlSession.getMapper(mapperClazz);
		}finally {
			sqlSession.close();
		}
		return mapper;
	}
	
	public SqlSessionFactory getSqlSessionFactory() {
		Assert.notNull(this.connectBean, "MyBatisConnector.connectBean has not initialized.");
		return this.connectBean.getSqlSessionFactory();
	}
	
	public void setConnectBean(MybatisConnectBean connectBean) {
		this.connectBean = connectBean;
	}
}
