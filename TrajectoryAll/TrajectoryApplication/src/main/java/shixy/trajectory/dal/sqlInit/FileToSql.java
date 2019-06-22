package shixy.trajectory.dal.sqlInit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.dbcp.BasicDataSource;

import shixy.trajectory.bean.Person;
import shixy.trajectory.util.GPSUtill;

/**
 * 数据库和文件操作
* @author sxy  
* @date 2018年5月30日
 */
public class FileToSql {
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	static final String user = "root";
	static final String password = "123456";
	static final String oriurl = "jdbc:mysql://localhost:3306/mysql?useSSL=true";
	static final String url = "jdbc:mysql://localhost:3306/trajectory_baidu?useSSL=true";
	static final String driver = "com.mysql.jdbc.Driver";
	static BasicDataSource ds = new BasicDataSource();
	static int count = 0;

	static {
		InitDatabase();
		try {
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void InitDatabase() {/* 判断数据库trajectory是否存在，不存在则创建 */
		StringBuilder check_db_sql = new StringBuilder(
				"select SCHEMA_NAME from information_schema.schemata where SCHEMA_NAME='trajectory_baidu';");
		try {
			Connection con;
			ResultSet re;
			Statement statement;
			Class.forName(driver);
			con = DriverManager.getConnection(oriurl, user, password);
			statement = con.createStatement();
			re = statement.executeQuery(check_db_sql.toString());
			if (!re.next()) {
				statement.executeUpdate("create database trajectory_baidu");
				re = statement.executeQuery(check_db_sql.toString());
				if (re.next()) {
					System.out.println("创建数据库" + re.getString(1) + "成功");
				} else
					System.out.println("数据库trajectory_baidu创建失败");
			} else {
				System.out.println("数据库trajectory_baidu" + re.getString(1) + "存在");
			}
			re.close();
			statement.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void InitTable(int n) {
		System.out.println("初始化表" + n);
		try {
			Connection con;
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			if(!HasTable("guide")) {
				stmt.executeUpdate("create table guide (num int primary key, table_name varchar(45));");
			}
			stmt.executeUpdate("insert ignore into guide (num,table_name) values (" + n + ", 'table_" + n + "');");
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!HasTable("table_" + n)) {
			CreatTable(n);
		}
	}

	public static boolean HasTable(String table_name) {// 判断是否有表n
		try {
			Connection con = ds.getConnection();
			StringBuilder checkTable = new StringBuilder("show tables like '");
			checkTable.append(table_name).append("';");
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(checkTable.toString());
			if (resultSet.next()) {// 表n存在
				stmt.close();
				con.close();
				return true;
			} else {// 不存在
				stmt.close();
				con.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void CreatTable(int n) {// 创建表n
		StringBuilder table_name = new StringBuilder("table_");
		table_name.append(n);
		try {
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
			StringBuffer createTabel = new StringBuffer("create table " + table_name
					+ " (num int,lat double,lng double,height double,rel_time double,time datetime primary key);");
			stmt.executeUpdate(createTabel.toString());
			createTabel = new StringBuffer("create table " + table_name.append("_timedis") + " (time int, num int);");
			stmt.executeUpdate(createTabel.toString());
			ReadFile(n);// 向数据库表n中写数据
			stmt.close();
			con.close();
		} catch (Exception e) {

		}
	}

	public static Person ReadFile(int n) {
		// 读文件,填充数据库；
		StringBuilder path = new StringBuilder("src\\main\\resources\\Geolife Trajectories 1.3\\Data");// �켣����·��
		Person per = new Person();
		File d = new File(path.toString());
		if (!d.exists()) {
			System.out.println(path + " not exists");
			return null;
		}
		File data[] = d.listFiles();// data[0]为000目录
		File trajectory[] = data[n].listFiles();// trajectory[]是000下目录，一般只有一个
		File trason;
		for (int i = 0;; i++) {// 确保下一级是目录
			trason = trajectory[i];
			if (trason.isDirectory())
				break;
		}
		File dam[] = trason.listFiles();
		BufferedReader reader = null;
		String tempString;
		int no2_num = 0;
		try {
			PreparedStatement psql;
			Connection conn = ds.getConnection();
			conn.setAutoCommit(false);
			psql = conn.prepareStatement("insert ignore into table_" + n + " values(?,?,?,?,?,?);");
			for (int k = 0; k < dam.length; k++) {// 生成第i个人完整的记录
				reader = new BufferedReader(new FileReader(dam[k]));// 第k个文件
				for (int i = 0; i < 7; i++) // 空读前6行
					reader.readLine();
				System.out.println("写数据" + k + ": " + df.format(new Date()));// new Date()为获取当前系统时间
				while ((tempString = reader.readLine()) != null) {// 从第7行读到最后一行
					String a[] = tempString.split(",");
					double b[];
					psql.setDouble(1, no2_num++);
					b = GPSUtill.gps84_To_bd09(Double.valueOf(a[0]), Double.valueOf(a[1]));// 百度坐标系换算
					psql.setDouble(2, b[0]);
					psql.setDouble(3, b[1]);
					psql.setDouble(4, Double.valueOf(a[3]));
					psql.setDouble(5, Double.valueOf(a[4]));
					psql.setString(6, a[5] + ' ' + a[6]);
					per.addTimedis((Integer.parseInt(a[6].substring(0, 2)) + 8) % 24);// GMT时间换算
					psql.addBatch();
					if (no2_num % 10000 == 0) {
						psql.executeBatch();
						conn.commit();
						psql.clearBatch(); //  最后将Batch清理掉
						System.out.println(no2_num + ": " + df.format(new Date()));// new Date()为获取当前系统时间
					}
				}
				reader.close();
			}
			psql.executeBatch();
			conn.commit();
			psql.clearBatch(); // 最后将Batch清理掉
			conn.setAutoCommit(true);
			Statement ps = conn.createStatement();
			System.out.println("开始插入时间统计");
			int sum = 0;
			for (int i = 0; i < 24; i++) {
				ps.executeUpdate(
						"insert into table_" + n + "_timedis" + " values(" + i + "," + per.getTimeNum(i) + ");");
				sum += per.getTimeNum(i);
				System.out.println("i=" + i + ": " + per.getTimeNum(i));
			}
			ps.executeUpdate("insert into table_" + n + "_timedis" + " values(" + 24 + "," + sum + ");");
			System.out.println("\n时间统计成功");// new Date()为获取当前系统时间
			if (reader != null)
				reader.close();
			psql.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQL异常");
		} catch (IOException e) {
			System.out.println("IO异常");
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException");
		} finally {

		}
		return per;
	}
}