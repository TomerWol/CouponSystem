package db.build;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * This is a Table Builder application for the DB.</br>
 * Each static method builds a specific Table in the 'Coupon System' DB.</br>
 * Don't forget to use it to create the tables before running the Tests for this
 * application.
 */
public class TableBuilder {

	public static void main(String[] args) {

		File driverFile = new File("files/driverName");
		File dbUrlFile = new File("files/dbUrl");

		// driver loading to memory.
		try (Scanner sc = new Scanner(driverFile);) {

			String driverName = sc.nextLine();
			Class.forName(driverName);
			System.out.println("Driver class loaded");

		} catch (FileNotFoundException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		// connecting to DB and using static methods to create the tables in the
		// DB
		Connection con = null;
		try (Scanner sc = new Scanner(dbUrlFile);) {

			String dbUrl = sc.nextLine();
			dbUrl += ";create=true";
			con = DriverManager.getConnection(dbUrl);
			createCopmanyTable(con);
			createCustomerTable(con);
			createCouponTable(con);
			createCompanyCouponTable(con);
			createCustomerCouponTable(con);

		} catch (FileNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// a method that creates a company table in the DB.
	private static void createCopmanyTable(Connection con) throws SQLException {
		String sql = "CREATE TABLE Company(" + "id BIGINT PRIMARY KEY," + "comp_name VARCHAR(50),"
				+ "password VARCHAR(50)," + "email VARCHAR(50))";

		Statement stmt = con.createStatement();
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	// a method that creates a customer table in the DB.
	private static void createCustomerTable(Connection con) throws SQLException {
		String sql = "CREATE TABLE Customer(" + "id BIGINT PRIMARY KEY," + "cust_name VARCHAR(50),"
				+ "password VARCHAR(50))";

		Statement stmt = con.createStatement();
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	// a method that creates a coupon table in the DB.
	private static void createCouponTable(Connection con) throws SQLException {
		String sql = "CREATE TABLE Coupon(" + "id BIGINT PRIMARY KEY," + "title VARCHAR(50)," + "start_date DATE,"
				+ "end_date DATE," + "amount INTEGER," + "type VARCHAR(50)," + "message VARCHAR(50)," + "price DOUBLE,"
				+ "image VARCHAR(50))";

		Statement stmt = con.createStatement();
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	// a method that creates a Company_Coupon table in the DB.
	private static void createCompanyCouponTable(Connection con) throws SQLException {
		String sql = "CREATE TABLE Company_Coupon(" + "comp_id BIGINT," + "coupon_id BIGINT,"
				+ "PRIMARY KEY(comp_id, coupon_id)" + ")";
		Statement stmt = con.createStatement();
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	// a method that creates a Customer_Coupon table in the DB.
	private static void createCustomerCouponTable(Connection con) throws SQLException {
		String sql = "CREATE TABLE Customer_Coupon(" + "cust_id BIGINT," + "coupon_id BIGINT,"
				+ "PRIMARY KEY(cust_id, coupon_id))";

		Statement stmt = con.createStatement();
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}
}
