package core.dao;

import java.util.Collection;

import core.beans.Coupon;
import core.beans.CouponType;
import core.exception.CouponSystemException;

/**
 * Interface that allows the application to perform all essential methods that
 * involve communication with the Coupon Table in the DB.
 */
public interface CouponDAO {
	
	public void createCoupon(Coupon coupon) throws CouponSystemException;

	public void removeCoupon(Coupon coupon) throws CouponSystemException;

	public void updateCoupon(Coupon coupon) throws CouponSystemException;
	
	public Coupon getCoupon(long id) throws CouponSystemException;

	public Collection<Coupon> getAllCoupons() throws CouponSystemException;
	
	public Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException;
	
}
