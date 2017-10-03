package core.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import core.beans.Coupon;
import core.beans.CouponType;
import core.beans.Customer;
import core.dao.db.CouponDBDAO;
import core.dao.db.CustomerDBDAO;
import core.dao.db.JoinTablesDBDAO;
import core.exception.CouponSystemException;

/**
 * This class implements {@link CouponClientFacade} together with
 * {@link CompanyFacade} and {@link AdminFacade}.</br>
 * The CustomerFacade is part of the Facades layer which enables the users to
 * handle their information.</br>
 * After a successful login through the singleton class {@link CouponSystem} you
 * get a CustomerFacade.</br>
 * This Facade contains all the methods that can execute your needs as a
 * Customer user.
 */
public class CustomerFacade implements CouponClientFacade {
	// Attributes
	private Customer customerDetails = null;
	private static CustomerDBDAO custdao = new CustomerDBDAO();
	private static CouponDBDAO coupdao = new CouponDBDAO();
	private static JoinTablesDBDAO joinTablesdao = new JoinTablesDBDAO();

	// Private CTOR
	private CustomerFacade(String name) throws CouponSystemException {
		System.out.println("Loged in as Customer : " + name);
		customerDetails = custdao.getCustomer(name);
	}

	// Methods
	/**
	 * This is a static method that the singleton class {@link CouponSystem}
	 * uses, through it you login, If your user name and password (the
	 * parameters the method gets) are correct the methods returns a
	 * Facade.</br>
	 * If the parameters are incorrect it will throw a
	 * {@link CouponSystemException}.</br>
	 * 
	 * @param username
	 *            A String of the user name you want to login with.
	 * @param password
	 *            A String of the user's password you want to login with.
	 * @return On a successful login returns CustomerFacade.
	 */
	public static CustomerFacade login(String username, String password) throws CouponSystemException {
		boolean check = custdao.login(username, password);
		if (check) {
			return new CustomerFacade(username);
		} else {
			throw new CouponSystemException("Failed to login as Customer");
		}
	}

	/**
	 * With this method the Customer user can buy a new Coupon.</br>
	 * <b>Be aware :</b> if the customer already got this coupon or the coupon
	 * is out of stock or it expired It will throw an
	 * {@link CouponSystemException}
	 * 
	 * @param coupon
	 *            A Coupon object that you want to create
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		// check if the coupon exists if not it will throw exception.
		Coupon couponFromDB = coupdao.getCoupon(coupon.getId());
		/*
		 * checks if the coupons's name from the parameter we got is match to
		 * the name of the same coupons id from the DB and from the parameter.
		 **/
		boolean checkIdName = couponFromDB.getTitle().equals(coupon.getTitle());
		if (checkIdName) {
			// check if the customer doesn't have that coupon already.
			Collection<Coupon> coupons = custdao.getCoupons(customerDetails);
			for (Coupon couponCheck : coupons) {
				if (couponCheck.getId() == coupon.getId()) {
					throw new CouponSystemException("Customer - " + customerDetails.getCustName()
							+ " already have this Coupon - " + coupon.getTitle());
				}
			}
		} else {
			throw new CouponSystemException("ID is not match to coupon's title");
		}
		// check if this coupon is not out of stock.
		if (couponFromDB.getAmount() == 0) {
			throw new CouponSystemException("this Coupon is out of stock");
		}
		// check coupon expiration day
		Date today = Calendar.getInstance().getTime();
		if (today.after(couponFromDB.getEndDate())) {
			throw new CouponSystemException("this coupon " + coupon.getTitle() + " expired");
		}

		// after all that its clear to purchase & update the amount of that
		// coupon.
		couponFromDB.setAmount(couponFromDB.getAmount() - 1);
		coupdao.updateCoupon(couponFromDB);
		joinTablesdao.createCustomerCoupon(customerDetails, couponFromDB);
		System.out.println("Purchased Coupon " + coupon.getTitle() + " successfully");
	}

	/**
	 * With this method the Customer user can get a list of all the customer's
	 * coupons.</br>
	 * If there are no coupons yet the list will be null.
	 * 
	 * @return A Collection list of Coupon with all the customer's coupons in
	 *         it.
	 */
	public Collection<Coupon> getCustomerCoupons() throws CouponSystemException {
		Collection<Coupon> customerCoupons = custdao.getCoupons(customerDetails);
		return customerCoupons;
	}

	/**
	 * With this method the Customer user can get a list of all the customer's
	 * coupons by a specific {@link CouponType}.</br>
	 * If there are no coupons with that type yet the list will be null.
	 * 
	 * @param type
	 *            A {@link CouponType} that you want to get a list of.
	 * 
	 * @return A Collection list of Coupon with all the coupons by that type in
	 *         it.
	 */
	public Collection<Coupon> getCustomerCouponsByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> customerCoupons = custdao.getCoupons(customerDetails);
		Collection<Coupon> customerCouponsByType = new ArrayList<>();

		for (Coupon coupon : customerCoupons) {
			if (coupon.getType().equals(type)) {
				customerCouponsByType.add(coupon);
			}
		}
		return customerCouponsByType;
	}

	/**
	 * With this method the Customer user can get a list of all the customer's
	 * coupons by a specific coupon price.</br>
	 * If there are no coupons with that price yet the list will be null.
	 * 
	 * @param price
	 *            A double value of that coupon price you want to get a list of.
	 * 
	 * @return A Collection list of Coupon with all the coupons by that price in
	 *         it.
	 */
	public Collection<Coupon> getCustomerCouponsByPrice(double price) throws CouponSystemException {
		Collection<Coupon> customerCoupons = custdao.getCoupons(customerDetails);
		Collection<Coupon> customerCouponsByPrice = new ArrayList<>();

		for (Coupon coupon : customerCoupons) {
			if (coupon.getPrice() == price) {
				customerCouponsByPrice.add(coupon);
			}
		}
		return customerCouponsByPrice;
	}
}