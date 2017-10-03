package core.dao;

import java.util.Collection;

import core.beans.Coupon;
import core.beans.Customer;
import core.exception.CouponSystemException;

/**
 * Interface that allows the application to perform all essential methods that
 * involve communication with the Customer Table in the DB.
 */
public interface CustomerDAO {

	public void createCustomer(Customer customer) throws CouponSystemException;

	public void removeCustomer(Customer customer) throws CouponSystemException;

	public void updateCustomer(Customer customer) throws CouponSystemException;

	public Customer getCustomer(long id) throws CouponSystemException;

	public Collection<Customer> getAllCustomers() throws CouponSystemException;

	public Collection<Coupon> getCoupons(Customer customer) throws CouponSystemException;

	public boolean login(String custName, String password) throws CouponSystemException;

}
