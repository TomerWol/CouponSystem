package core.facade;

import java.util.ArrayList;
import java.util.Collection;
import core.beans.Company;
import core.beans.Coupon;
import core.beans.CouponType;
import core.dao.db.CompanyDBDAO;
import core.dao.db.CouponDBDAO;
import core.dao.db.JoinTablesDBDAO;
import core.exception.CouponSystemException;

/**
 * This class implements {@link CouponClientFacade} together with
 * {@link CustomerFacade} and {@link AdminFacade}.</br>
 * The CompanyFacade is part of the Facades layer which enables the users to
 * handle their information.</br>
 * After a successful login through the singleton class {@link CouponSystem} you
 * get a CompanyFacade.</br>
 * This Facade contains all the methods that can execute your needs as a Company
 * user.
 */
public class CompanyFacade implements CouponClientFacade {
	// Attributes
	private Company companyDetails = null;
	private static CompanyDBDAO compdao = new CompanyDBDAO();
	private static CouponDBDAO coupdao = new CouponDBDAO();
	private static JoinTablesDBDAO tablesdao = new JoinTablesDBDAO();

	// Private CTOR
	private CompanyFacade(String name) throws CouponSystemException {
		System.out.println("Loged in as Company : " + name);
		companyDetails = compdao.getCompany(name);
	}

	// methods
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
	 * @return On a successful login returns CompanyFacade.
	 */
	public static CompanyFacade login(String username, String password) throws CouponSystemException {
		boolean check = compdao.login(username, password);
		if (check) {
			return new CompanyFacade(username);
		} else {
			throw new CouponSystemException("Failed to login as Company");
		}
	}

	/**
	 * With this method the Company user can create a new Coupon.</br>
	 * <b>Be aware :</b> if the title of this coupon already exists it will
	 * throw an {@link CouponSystemException}
	 * 
	 * @param coupon
	 *            A Coupon object that you want to create
	 */
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		// checks if the title exists
		Collection<Coupon> coupons = coupdao.getAllCoupons();
		for (Coupon couponCheck : coupons) {
			boolean check = couponCheck.getTitle().equals(coupon.getTitle());
			if (check) {
				throw new CouponSystemException("Coupon " + coupon.getTitle() + " already exist");
			}
		}

		// if we are here that coupon title doesn't exists.
		coupdao.createCoupon(coupon);
		tablesdao.createCompanyCoupon(companyDetails, coupon);
		System.out.println("Created Coupon " + coupon.getTitle() + " successfully");
	}

	/**
	 * With this method the Company user can delete a Coupon.</br>
	 * <b>Be aware :</b> if you delete a coupon all the customers who bought
	 * that coupon from this company will be deleted as well.
	 * 
	 * @param coupom
	 *            A coupon object that you want to delete.
	 */
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		// checks if that coupon exists or will throw exception.
		Coupon couponFromDB = coupdao.getCoupon(coupon.getId());
		/*
		 * checks if the coupon's title from the parameter we got is match to
		 * the title of the same coupons id from the DB and from the parameter.
		 **/
		if (couponFromDB.getTitle().equals(coupon.getTitle())) {
			tablesdao.deleteCompanyCoupon(couponFromDB);
			tablesdao.deleteCustomerCoupon(couponFromDB);
			coupdao.removeCoupon(couponFromDB);
			System.out.println("Deleted Coupon " + coupon.getTitle() + " successfully");
		} else {
			throw new CouponSystemException("ID not match to coupon's Title !");
		}
	}

	/**
	 * With this method the Company user can update a specific Coupon
	 * details.</br>
	 * This method will only update the coupon's endDate & price.
	 * 
	 * @param coupon
	 *            A Coupon object that you want to update.
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		// checks if that coupon exists or will throw exception.
		Coupon couponDB = coupdao.getCoupon(coupon.getId());
		/*
		 * checks if the customer's name from the parameter we got is match to
		 * the name of the same customers id from the DB and from the parameter.
		 **/
		if (couponDB.getTitle().equals(coupon.getTitle())) {
			couponDB.setEndDate(coupon.getEndDate());
			couponDB.setPrice(coupon.getPrice());
			coupdao.updateCoupon(couponDB);
			System.out.println("Updated Coupon " + coupon.getTitle() + " successfully");
		} else {
			throw new CouponSystemException("ID not match to coupon's Title !");
		}
	}

	/**
	 * With this method the Company user gets a specific Coupon through the
	 * coupons's ID that the method gets.</br>
	 * If there is no coupon with that ID it will throw an
	 * {@link CouponSystemException}.
	 * 
	 * @param id
	 *            A long of the coupon's ID that you want to get.
	 * @return A Coupon object that you want to get.
	 */
	public Coupon getCoupon(Long id) throws CouponSystemException {
		Collection<Coupon> companyCoupons = compdao.getCouppons(companyDetails);
		Coupon coupon = null;
		for (Coupon couponCheck : companyCoupons) {
			if (couponCheck.getId() == id) {
				coupon = couponCheck;
			}
		}
		if (coupon == null) {
			throw new CouponSystemException("Coupon id " + id + " not found in the companies coupon");
		}
		return coupon;
	}

	/**
	 * With this method the Company user can get a list of all the company's
	 * coupons.</br>
	 * If there are no coupons yet the list will be null.
	 * 
	 * @return A Collection list of Coupon with all the coupons in it.
	 */
	public Collection<Coupon> getAllCompanyCoupons() throws CouponSystemException {
		Collection<Coupon> companyCoupons = compdao.getCouppons(companyDetails);
		return companyCoupons;
	}

	/**
	 * With this method the Company user can get a list of all the company's
	 * coupons by a specific {@link CouponType}.</br>
	 * If there are no coupons with that type yet the list will be null.
	 * 
	 * @param type
	 *            A {@link CouponType} that you want to get a list of.
	 * 
	 * @return A Collection list of Coupon with all the coupons by that type in
	 *         it.
	 */
	public Collection<Coupon> getAllCompanyCouponsByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> companyCoupons = compdao.getCouppons(companyDetails);
		Collection<Coupon> companyCouponsByType = new ArrayList<>();

		for (Coupon coupon : companyCoupons) {
			if (coupon.getType().equals(type)) {
				companyCouponsByType.add(coupon);
			}
		}
		return companyCouponsByType;
	}

	/**
	 * With this method the Company user can get a list of all the company's
	 * coupons by a specific price.</br>
	 * If there are no coupons with that price yet the list will be null.
	 * 
	 * @param type
	 *            A double value of the coupon price that you want to get a list
	 *            of.
	 * 
	 * @return A Collection list of Coupon with all the coupons by that price in
	 *         it.
	 */
	public Collection<Coupon> getAllCompanyCouponsByPrice(double price) throws CouponSystemException {
		Collection<Coupon> companyCoupons = compdao.getCouppons(companyDetails);
		Collection<Coupon> companyCouponsByPrice = new ArrayList<>();

		for (Coupon coupon : companyCoupons) {
			if (coupon.getPrice() == price) {
				companyCouponsByPrice.add(coupon);
			}
		}
		return companyCouponsByPrice;
	}

}
