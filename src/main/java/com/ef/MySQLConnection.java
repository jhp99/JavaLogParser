package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnection {

	private static String DB_NAME = "dblogs";
	private static String PASSWORD = "123";
	private static String USERNAME = "root";
	private static String TABLE_NAME = "rawdblogs";
	private static String BLOCKED_IP_TABLE = "robotip";

	public void insertRawLogData(List<SqlModel> data) throws SQLException, ClassNotFoundException {

		Connection dbConnection = this.getDBConnection();

		String insertString = "INSERT INTO " + TABLE_NAME + " (date,ip_address,request_type,status_code,user_agent) "
				+ " VALUES (?,?,?,?,?) ";

		PreparedStatement insert = dbConnection.prepareStatement(insertString);
		for (SqlModel value : data) {
			insert.setTimestamp(1, value.getDate());
			insert.setString(2, value.getIpAddress());
			insert.setString(3, value.getRequestType());
			insert.setInt(4, value.getStatusCode());
			insert.setString(5, value.getUserAgent());
			insert.addBatch();
		}
		insert.executeBatch();
		dbConnection.commit();
		dbConnection.close();
	}

	public List<RobotIP> searchData(Timestamp startDate, String endTime, int threshold)
			throws SQLException, ClassNotFoundException {

		if (!(endTime.toUpperCase().equals("DAILY") || endTime.toUpperCase().equals("HOURLY"))) {
			throw new RuntimeException("Invalid Duration");
		}

		Connection dbConnection = this.getDBConnection();

		int durationHours = (endTime.toUpperCase().equals("DAILY")) ? 24 : 1;

		Timestamp endDate = new Timestamp(startDate.getTime() + (1000 * 60 * 60 * durationHours));

		String searchString = "SELECT ip_address , COUNT(ip_address) as robots " + " FROM " + TABLE_NAME
				+ " WHERE  date BETWEEN ? and ? " + " GROUP BY ip_address " + " HAVING robots >= ? ";

		PreparedStatement prepareSearchStatement = dbConnection.prepareStatement(searchString);
		prepareSearchStatement.setTimestamp(1, startDate);
		prepareSearchStatement.setTimestamp(2, endDate);
		prepareSearchStatement.setInt(3, threshold);

		ResultSet rs = prepareSearchStatement.executeQuery();
		List<RobotIP> robotip = new ArrayList<>();
		while (rs.next()) {
			
			String count = rs.getString("robots");
			String ipAddress = rs.getString("ip_address");
			
			robotip.add(new RobotIP(ipAddress, count,startDate,endDate));

		}

		dbConnection.commit();
		dbConnection.close();
		return robotip;
	}

	public void insertRobotIP(List<RobotIP> robotip) throws ClassNotFoundException, SQLException {
		
		Connection dbConnection = this.getDBConnection();

		String insertStringIP = "INSERT INTO " + BLOCKED_IP_TABLE + " (ip_address,comment) " + " VALUES (?,?) ";
		
		PreparedStatement insertIP = dbConnection.prepareStatement(insertStringIP);

		for (RobotIP ips : robotip) {
			insertIP.setString(1, ips.getIpAddress());
			insertIP.setString(2, ips.toString());
			
			insertIP.addBatch();
		}
		insertIP.executeBatch();
		
		dbConnection.commit();
		dbConnection.close();
	}

	private Connection getDBConnection() throws ClassNotFoundException, SQLException {
		// DB Connection
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection dbConnection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/" + DB_NAME + "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);

		dbConnection.setAutoCommit(false);
		return dbConnection;

	}
}
