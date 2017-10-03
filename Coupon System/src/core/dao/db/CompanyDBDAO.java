package core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import core.beans.Company;
import core.beans.Coupon;
import core.dao.CompanyDAO;
import core.exception.CouponSystemException;
import db.connection_pool.ConnectionPool;

/**
 * This class implements {@linkplain CompanyDAO}.</br>
 * This class is used by the Facade classes to change the company data in the
 * DB.</br>
 * On creation the class gets {@link ConnectionPool} instance and through the
 * methods you can change the data.</br>
 * Each method at the beginning takes a connection and sends a command to the DB
 * such as : UPDATE, INSERT, DELETE, SELECT depends on your needs.</br>
 * After the method is finished the connection returns.
 */
public class CompanyDBDAO implements CompanyDAO {
	// Attributes
	private ConnectionPool pool = null;

	// CTOR
	public CompanyDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	// Methods
	/**
	 * This method sends a INSERT command to the DB with the values of the
	 * Company object that the method gets.</br>
	 * Creates a company in the Company table with values such as : ID , Company
	 * name , Password , Email.</br>
	 * If the method fails to create he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param company
	 *            The Company object that you want to create.
	 */
	@Override
	public void createCompany(Company company) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "INSERT INTO Company VALUES(?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, company.getId());
			stmt.setString(2, company.getCompName());
			stmt.setString(3, company.getPassword());
			stmt.setString(4, company.getEmail());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Create Copmany " + company.getCompName() + " Failed !(Already got this ID or Server is down)", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a DELETE command to the DB with the values of the
	 * Company object that the method gets.</br>
	 * Removes the company you gave from the Company table.</br>
	 * If the method fails to remove he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param company
	 *            the Company object that you want to remove.
	 */
	@Override
	public void removeCompany(Company company) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "DELETE FROM Company WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, company.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Remove Company " + company.getCompName() + " Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a UPDATE command to the DB with the values of the
	 * Company object that the method gets.</br>
	 * Updates the table of a specific Company values (Password, Name and
	 * etc).</br>
	 * If the method fails to update he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param company
	 *            the Company object that you want to update.
	 */
	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "UPDATE Company SET comp_name = ?, password = ?, email = ?  WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, company.getCompName());
			stmt.setString(2, company.getPassword());
			stmt.setString(3, company.getEmail());
			stmt.setLong(4, company.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Update Company " + company.getCompName() + " was Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a SELECT command to the DB.</br>
	 * Get a company from the Company Table with a specific ID(long) that the
	 * methods gets.</br>
	 * If the method fails to get the company he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param id
	 *            a long that represent the company ID that you want to get.
	 * @return Company object of that specific id.
	 */
	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		Company company = null;

		try {
			String sql = "SELECT * FROM Company WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String name, password, email;

				name = rs.getString(2);
				password = rs.getString(3);
				email = rs.getString(4);
				company = new Company(id, name, password, email);
				rs.close();
			} else {
				rs.close();
				throw new CouponSystemException("Company id " + id + " does not exist");
			}
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Get Company Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return company;
	}

	/**
	 * This method sends a SELECT command to the DB.</br>
	 * Get a company from the Company Table with a specific name that the method
	 * gets.</br>
	 * If the method fails to get the company he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param name
	 *            a String of the company name that you want to get.
	 * @return Company object of that specific name.
	 */
	public Company getCompany(String name) throws CouponSystemException {
		Connection con = pool.getConnection();
		Company company = null;

		try {
			String sql = "SELECT * FROM Company WHERE comp_name = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String password, email;
				long id;

				id = rs.getLong(1);
				password = rs.getString(3);
				email = rs.getString(4);
				company = new Company(id, name, password, email);
				rs.close();
			} else {
				rs.close();
				throw new CouponSystemException("Company name " + name + "does not exist");
			}
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Get Company " + name + " failed", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return company;
	}

	/**
	 * This method sends a SELECT command to the DB to get all the companies
	 * from the Company Table.</br>
	 * The method then puts them in Collection list and returns it.</br>
	 * If the method fails to get the companies he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @return Collection list of Company with all the companies.
	 */
	@Override
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Connection con = pool.getConnection();
		Collection<Company> companies = new ArrayList<>();

		try {
			String sql = "SELECT * FROM Company";
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			boolean check = rs.next();
			while (check) {
				long id = rs.getLong(1);
				companies.add(getCompany(id));
				check = rs.next();
			}
			rs.close();
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Failed to get all the Companies !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return companies;
	}

	/**
	 * This method sends a SELECT command to the DB to get all the coupons from
	 * the Company Coupon Table of a specific Company.</br>
	 * The method then puts them in a Collection list.</br>
	 * If the method fails to get the coupons he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param company
	 *            a company object that you want to get coupons from.
	 * @return Collection list of Coupon with all the company coupons.
	 */
	@Override
	public Collection<Coupon> getCouppons(Company company) throws CouponSystemException {
		Connection con = pool.getConnection();
		Collection<Coupon> coupons = new ArrayList<>();
		CouponDBDAO coup = new CouponDBDAO();

		try {
			String sql = "SELECT * FROM Company_Coupon WHERE comp_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, company.getId());
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
	 * company name and password in the Company Table. </br>
	 * If there are a company name and password in the DB, The method sends you
	 * back true.</br>
	 * If the parameters(compName, password) that the method gets are incorrect
	 * or not in the Company Table, The method will thorw a
	 * {@link CouponSystemException}
	 * 
	 * @param compName
	 *            a string of the company name.
	 * @param password
	 *            a string of the company password.
	 * @return true if the company name & password are correct or throws a
	 *         CouponSystemException.
	 */
	@Override
	public boolean login(String compName, String password) throws CouponSystemException {
		Connection con = pool.getConnection();
		boolean exist = false;

		try {
			String sql = "SELECT * FROM Company WHERE comp_name = ? AND password = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, compName);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			exist = rs.next();
			rs.close();
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Server is down");
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return exist;
	}
}
