package core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import core.beans.Coupon;
import core.beans.Customer;
import core.dao.CustomerDAO;
import core.exception.CouponSystemException;
import db.connection_pool.ConnectionPool;

/**
 * This class implements {@linkplain CustomerDAO}.</br>
 * This class is used by the Facade classes to change the customer data in the
 * DB.</br>
 * On creation the class gets {@link ConnectionPool} instance and through the
 * methods you can change the data.</br>
 * Each method at the beginning takes a connection and sends a command to the DB
 * such as : UPDATE, INSERT, DELETE, SELECT depends on your needs.</br>
 * After the method is finished the connection returns.
 */
public class CustomerDBDAO implements CustomerDAO {
	// Attributes
	private ConnectionPool pool = null;

	// CTOR
	public CustomerDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	// Methods
	/**
	 * This method sends a INSERT command to the DB with the values of the
	 * Customer object that the method gets.</br>
	 * Creates a customer in the Customer Table with values such as : ID, Name,
	 * Password.</br>
	 * If the method fails to create he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param customer
	 *            The Customer object that you want to create in the table.
	 */
	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "INSERT INTO Customer VALUES(?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, customer.getId());
			stmt.setString(2, customer.getCustName());
			stmt.setString(3, customer.getPassword());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Create Customer " + customer.getCustName() + " Failed ! (Already got this ID or Server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a DELETE command to the DB with the values of the
	 * Customer object that the method gets.</br>
	 * Removes a customer from the Customer Table.</br>
	 * If the method fails to remove he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param customer
	 *            The Customer object the you want to remove from the table.
	 */
	@Override
	public void removeCustomer(Customer customer) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "DELETE FROM Customer WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, customer.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Remove Customer " + customer.getCustName() + " Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a UPDATE command to the DB with the values of the
	 * Customer object that the method gets.</br>
	 * Updates a specific customer from the Customer Table.</br>
	 * If the method fails to update he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param customer
	 *            The Customer object that you want to update.
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "UPDATE Customer SET cust_name = ?, password = ?  WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, customer.getCustName());
			stmt.setString(2, customer.getPassword());
			stmt.setLong(3, customer.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Update Customer " + customer.getCustName() + " was Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a SELECT command to the DB with a long value of the
	 * customer id.</br>
	 * Get a customer from Customer Table with a specific id.</br>
	 * If the method fails to get the customer or the customer doesn't exist in
	 * the DB it will throw an {@link CouponSystemException}.
	 * 
	 * @param id
	 *            A long value of the id customer that you want to get.
	 * @return A Customer object with that specific id .
	 */
	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		Customer customer = null;

		try {
			String sql = "SELECT * FROM Customer WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String custName, password;

				custName = rs.getString(2);
				password = rs.getString(3);
				customer = new Customer(id, custName, password);
				rs.close();
			} else {
				rs.close();
				throw new CouponSystemException("Customer id " + id + " not found");
			}

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Get Customer id : " + id + " Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return customer;
	}

	/**
	 * This method sends a SELECT command to the DB with a String value of the
	 * customer name.</br>
	 * get a customer from the Customer Table with a specific name.</br>
	 * If the method fails to get the customer or the customer doesn't exist in
	 * the DB it will throw an {@link CouponSystemException}.
	 * 
	 * @param name
	 *            A String of the customer name
	 * @return A Customer object with that specific customer.
	 */
	public Customer getCustomer(String name) throws CouponSystemException {
		Connection con = pool.getConnection();
		Customer customer = null;

		try {
			String sql = "SELECT * FROM Customer WHERE cust_name = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String password;
				Long id;
				id = rs.getLong(1);
				password = rs.getString(3);
				customer = new Customer(id, name, password);
				rs.close();
			} else {
				rs.close();
				throw new CouponSystemException("Customer " + name + " not found");
			}

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Get Customer name : " + name + " Failed !",
					e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return customer;
	}

	/**
	 * This method sends a SELECT command to the DB to get all the customers
	 * from the Customer Table.</br>
	 * The method then puts them in a Collection list and returns it.</br>
	 * If the method fails to get the customers he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @return Collection list of Customer with all the customers.
	 */
	@Override
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		Connection con = pool.getConnection();
		Collection<Customer> customers = new ArrayList<>();

		try {
			String sql = "SELECT * FROM Customer";
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			boolean check = rs.next();
			while (check) {
				long id = rs.getLong(1);
				customers.add(getCustomer(id));
				check = rs.next();
			}
			rs.close();
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Failed to get all the Customers !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return customers;
	}

	/**
	 * This method sends a SELECT command to the DB to get all the coupons from
	 * the Customers Coupon Table of a specific Customer.</br>
	 * The method then puts them in a Collection list.</br>
	 * If the method fails to get the coupons he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param customer
	 *            A Customer object that you want to get all his coupons.
	 * @return A Collection list of Coupon.
	 */
	@Override
	public Collection<Coupon> getCoupons(Customer customer) throws CouponSystemException {
		Connection con = pool.getConnection();
		CouponDBDAO coup = new CouponDBDAO();
		Collection<Coupon> coupons = new ArrayList<>();

		try {
			String sql = "SELECT * FROM Customer_Coupon WHERE cust_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, customer.getId());
			ResultSet rs = stmt.executeQuery();
			boolean check = rs.next();
			while (check) {
				long id = rs.getLong(2);
				coupons.add(coup.getCoupon(id));
				check = rs.next();
			}
			rs.close();
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Failed to get all the Coupons !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return coupons;
	}

	/**
	 * This method sends a SELECT command to the DB to check if there are a
	 * customer name and password in the Customer Table. </br>
	 * If there are a company name and password in the DB, The method sends you
	 * back true.</br>
	 * If the parameters(compName, password) that the method gets are incorrect
	 * or not in the Company Table, The method will throw a
	 * {@link CouponSystemException}
	 * 
	 * @param custName
	 *            a string of the customer name.
	 * @param password
	 *            a string of the customer password.
	 * @return true if the custName & password are correct or throws a
	 *         CouponSystemException if they are not.
	 */
	@Override
	public boolean login(String custName, String password) throws CouponSystemException {
		Connection con = pool.getConnection();
		boolean exist = false;
		try {
			String sql = "SELECT * FROM Customer WHERE cust_name = ? AND password = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, custName);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			exist = rs.next();
			rs.close();
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("server is down");
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}

		return exist;
	}

}
