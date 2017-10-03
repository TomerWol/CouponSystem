package core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import core.beans.Company;
import core.beans.Coupon;
import core.beans.Customer;
import core.dao.JoinTablesDAO;
import core.exception.CouponSystemException;
import db.connection_pool.ConnectionPool;

/**
 * This class implements {@linkplain JoinTablesDAO}.</br>
 * This class is used by the Facade classes to change the the Join
 * Tables(Custoer_Coupon, Copany_Coupon) and the Coupon table data in the
 * DB.</br>
 * On creation the class gets {@link ConnectionPool} instance and through the
 * methods you can change the data.</br>
 * Each method at the beginning takes a connection and sends a command to the DB
 * such as :DELETE, SELECT depends on your needs.</br>
 * After the method is finished the connection returns.
 */

public class JoinTablesDBDAO implements JoinTablesDAO {
	// Attributes
	private ConnectionPool pool = null;

	// CTOR
	public JoinTablesDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	// Methods
	/**
	 * This method sends a INSERT command to the DB with the values of the
	 * Customer object and Coupon object that the method gets.</br>
	 * Creates a customer coupon in the Customer Coupon Table with values such
	 * as : Customer ID, Coupon ID.</br>
	 * If the method fails to create he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param customer
	 *            A Customer object, the customer thats buying the coupon.
	 * @param coupon
	 *            A Coupon object, the coupon that the customer want to buy.
	 */
	@Override
	public void createCustomerCoupon(Customer customer, Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "INSERT INTO Customer_Coupon VALUES(?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, customer.getId());
			stmt.setLong(2, coupon.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Create Customer Coupon Failed !(Already got this ID / server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a INSERT command to the DB with the values of the
	 * Company object and Coupon object that the method gets.</br>
	 * Creates a company coupon in the Company Coupon Table with values such as
	 * : Company ID, Coupon ID.</br>
	 * If the method fails to create he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param company
	 *            A Company object, the Company that creating the Coupon.
	 * @param coupon
	 *            A Coupon object, the Coupon that the company created.
	 */
	@Override
	public void createCompanyCoupon(Company company, Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();
		try {
			String sql = "INSERT INTO Company_Coupon VALUES(?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, company.getId());
			stmt.setLong(2, coupon.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Create Company Coupon Failed !(Already got this ID / server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a DELETE command to the DB with the values of the
	 * Coupon object that the method gets.</br>
	 * Deletes a Coupon from the Company Coupon Table.</br>
	 * If the method fails to delete he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param coupon
	 *            A Coupon object that you want to delete.
	 */
	@Override
	public void deleteCompanyCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "DELETE FROM Company_Coupon WHERE coupon_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, coupon.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Delete Coupon " + coupon.getTitle() + " Failed !(Already got this ID / server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a DELETE command to the DB with the values of the
	 * Coupon object that the method gets.</br>
	 * Deletes a coupon from the Customer Coupon Table.</br>
	 * If the method fails to delete he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param coupon
	 *            A Coupon object that you want to delete.
	 */
	@Override
	public void deleteCustomerCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "DELETE FROM Customer_Coupon WHERE coupon_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, coupon.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Delete Coupon " + coupon.getTitle() + " Failed !(Already got this ID / server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

}
