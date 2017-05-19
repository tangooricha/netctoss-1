package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil {

	//连接池对象-由DBCP提供
	private static BasicDataSource ds;
	
	static {
		Properties p = new Properties();
		try {
			p.load(DBUtil.class.getClassLoader()
				.getResourceAsStream("db.properties"));
			//读取参数
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			String initsize = p.getProperty("initsize");
			String maxsize = p.getProperty("maxsize");
			//创建连接池
			ds = new BasicDataSource();
			//设置参数
			//使用这个参数注册驱动
			ds.setDriverClassName(driver);
			//使用这3个参数创建连接
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pwd);
			//使用其他参数管理连接
			ds.setInitialSize(new Integer(initsize));
			ds.setMaxActive(new Integer(maxsize));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
				"加载db.properties失败", e);
		}
		
	}
	
	public static Connection getConnection() 
		throws SQLException {
		return ds.getConnection();
	}
	
	/**
	 * 由连接池创建的连接,连接的close方法
	 * 被连接池重写了,变为了归还连接的逻辑,
	 * 即:连接池会将连接的状态设置为空闲,
	 * 并清空连接中所包含的任何数据.
	 */
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
					"归还连接失败", e);
			}
		}
	}
	
	public static void rollback(Connection conn) {
		if(conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
					"回滚失败", e);
			}
		}
	}
	
	public static void main(String[] args) 
		throws SQLException {
		Connection conn = 
			DBUtil.getConnection();
		System.out.println(conn);
		DBUtil.close(conn);
	}
	
}








