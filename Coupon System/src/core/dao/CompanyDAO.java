package core.dao;

import java.util.Collection;

import core.beans.Company;
import core.beans.Coupon;
import core.exception.CouponSystemException;

/**
 * Interface that allows the application to perform all essential methods that
 * involve communication with the Company Table in the DB.
 */
public interface CompanyDAO {

	public void createCompany(Company company) throws CouponSystemException;

	public void removeCompany(Company company) throws CouponSystemException;

	public void updateCompany(Company company) throws CouponSystemException;

	public Company getCompany(long id) throws CouponSystemException;

	public Collection<Company> getAllCompanies() throws CouponSystemException;

	public Collection<Coupon> getCouppons(Company company) throws CouponSystemException;

	public boolean login(String compName, String password) throws CouponSystemException;

}
