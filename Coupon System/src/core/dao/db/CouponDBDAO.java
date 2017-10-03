package core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import core.beans.Coupon;
import core.beans.CouponType;
import core.dao.CouponDAO;
import core.exception.CouponSystemException;
import db.connection_pool.ConnectionPool;

/**
 * This class implements {@linkplain CouponDAO}.</br>
 * This class is used by the Facade classes to change the coupon data in the
 * DB.</br>
 * On creation the class gets {@link ConnectionPool} instance and through the
 * methods you can change the data.</br>
 * Each method at the beginning takes a connection and sends a command to the DB
 * such as : UPDATE, INSERT, DELETE, SELECT depends on your needs.</br>
 * After the method is finished the connection returns.
 */
public class CouponDBDAO implements CouponDAO {
	// Attributes
	private ConnectionPool pool = null;

	// CTOR
	public CouponDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	// Methods
	/**
	 * This method sends a INSERT command to the DB with the values of the
	 * Coupon object that the method gets.</br>
	 * Creates a coupon in the Coupon Table with values such as : ID, Title,
	 * Start_Date, End_Date, Amount, Type, Message, Price, Image.</br>
	 * If the method fails to create he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param coupon
	 *            the Coupon object that you want to create in the table.
	 */
	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "INSERT INTO Coupon VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);

			java.sql.Date startDate = new java.sql.Date(coupon.getStartDate().getTime());
			java.sql.Date endDate = new java.sql.Date(coupon.getEndDate().getTime());

			stmt.setLong(1, coupon.getId());
			stmt.setString(2, coupon.getTitle());
			stmt.setDate(3, startDate);
			stmt.setDate(4, endDate);
			stmt.setInt(5, coupon.getAmount());
			stmt.setString(6, coupon.getType().toString());
			stmt.setString(7, coupon.getMessage());
			stmt.setDouble(8, coupon.getPrice());
			stmt.setString(9, coupon.getImage());
			stmt.executeUpdate();

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Create Coupon " + coupon.getTitle() + " Failed ! (Already got this ID or server is down)", e);
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
	 * Removes a specific coupon from the Coupon Table.</br>
	 * If the method fails to remove he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param coupon
	 *            the Coupon object that you want to remove.
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "DELETE FROM Coupon WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, coupon.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Remove Coupon " + coupon.getTitle() + " Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a UPDATE command to the DB with the values of the
	 * Coupon object that the method gets.</br>
	 * Updates a specific coupon values (amount, message etc)</br>
	 * If the method fails to update he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param coupon
	 *            the specific Coupon object that you want to update.
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = pool.getConnection();

		try {
			String sql = "UPDATE Coupon SET title = ?, start_date = ?, end_date = ?, amount = ?, type = ?, message = ?, "
					+ "price = ?, image = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);

			java.sql.Date startDate = new java.sql.Date(coupon.getStartDate().getTime());
			java.sql.Date endDate = new java.sql.Date(coupon.getEndDate().getTime());

			stmt.setString(1, coupon.getTitle());
			stmt.setDate(2, startDate);
			stmt.setDate(3, endDate);
			stmt.setInt(4, coupon.getAmount());
			stmt.setString(5, coupon.getType().toString());
			stmt.setString(6, coupon.getMessage());
			stmt.setDouble(7, coupon.getPrice());
			stmt.setString(8, coupon.getImage());
			stmt.setLong(9, coupon.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException(
					"Update Coupon " + coupon.getTitle() + " was Failed !", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This method sends a SELECT command to the DB with the long value of the
	 * coupon id.</br>
	 * Gets a coupon from the Coupon Table with a specific id.</br>
	 * If the method fails to get the coupon or the coupon doesn't exist in the
	 * DB it will throw an {@link CouponSystemException}.
	 * 
	 * @param id
	 *            the coupon id that you want to get.
	 * @return a Coupon object of a specific id or throws CouponSystemException
	 *         if that coupon not found.
	 */
	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		Coupon coupon = null;

		try {
			String sql = "SELECT * FROM Coupon WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String title, message, image;
				Date startDate, endDate;
				int amount;
				double price;
				CouponType type;

				title = rs.getString(2);
				startDate = new java.util.Date(rs.getDate(3).getTime());
				endDate = new java.util.Date(rs.getDate(4).getTime());
				amount = rs.getInt(5);
				type = CouponType.valueOf(rs.getString(6));
				message = rs.getString(7);
				price = rs.getDouble(8);
				image = rs.getString(9);

				coupon = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
				rs.close();
			} else {
				rs.close();
				throw new CouponSystemException("Coupon " + id + " not found");
			}

		} catch (SQLException e) {
			CouponSystemException couponSysEx = new CouponSystemException("Get coupon failed", e);
			throw couponSysEx;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return coupon;
	}

	/**
	 * This method sends a SELECT command to the DB to get all the coupons from
	 * the Coupon Table.</br>
	 * The method then puts them in a Collection list and returns it.</br>
	 * If the method fails to get the coupons he will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @return Collection list of Coupon.
	 */
	@Override
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		Connection con = pool.getConnection();
		Collection<Coupon> coupons = new ArrayList<>();

		try {
			String sql = "SELECT * FROM Coupon";
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			boolean check = rs.next();
			while (check) {
				long id = rs.getLong(1);
				coupons.add(getCoupon(id));
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
	 * This method uses the getAllCoupons() method and with that gives you a
	 * Collection list of coupons of a specific CouponType, see also
	 * {@link CouponType}, {@link getAllCoupons}.</br>
	 * If the method fails to get the coupons he will throw an
	 * {@link CouponSystemException}.</br>
	 * If there are no coupons by that CouponType he will throw an
	 * {@link CouponSystemException}.</br>
	 * 
	 * @param type
	 *            the {@link CouponType} that you want to get list of.
	 * @return a Collection list of Coupon with all the coupons of a specific
	 *         {@link CouponType}.
	 */
	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> allCoupons = getAllCoupons();
		Collection<Coupon> couponByType = new ArrayList<>();

		for (Coupon coupon : allCoupons) {
			if (type.equals(coupon.getType())) {
				couponByType.add(coupon);
			}
		}
		return couponByType;
	}

}
