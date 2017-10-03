package core.dao;

import core.beans.Company;
import core.beans.Coupon;
import core.beans.Customer;
import core.exception.CouponSystemException;

/**
 * Interface that allows the application to perform all essential methods that
 * involve communication with the Join Tables(Customer_Coupon, Company_Coupon)
 * in the DB.
 */
public interface JoinTablesDAO {

	public void createCustomerCoupon(Customer customer, Coupon coupon) throws CouponSystemException;

	public void createCompanyCoupon(Company company, Coupon coupon) throws CouponSystemException;

	public void deleteCompanyCoupon(Coupon coupon) throws CouponSystemException;

	public void deleteCustomerCoupon(Coupon coupon) throws CouponSystemException;
}
