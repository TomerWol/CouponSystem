package core.facade;

import java.util.Collection;
import core.beans.Company;
import core.beans.Coupon;
import core.beans.Customer;
import core.dao.db.CompanyDBDAO;
import core.dao.db.CouponDBDAO;
import core.dao.db.CustomerDBDAO;
import core.dao.db.JoinTablesDBDAO;
import core.exception.CouponSystemException;

/**
 * This class implements {@link CouponClientFacade} together with
 * {@link CompanyFacade} and {@link CustomerFacade}.</br>
 * The AdminFacade is part of the Facades layer which enables the users to
 * handle their information.</br>
 * After a successful login through the singleton class {@link CouponSystem} you
 * get a AdminFacade.</br>
 * This Facade contains all the methods that can execute your needs as a Admin
 * user.
 */
public class AdminFacade implements CouponClientFacade {

	// Attributes
	private static CustomerDBDAO custdao = new CustomerDBDAO();
	private static CouponDBDAO coupdao = new CouponDBDAO();
	private static CompanyDBDAO compdao = new CompanyDBDAO();
	private static JoinTablesDBDAO joinTtablesdao = new JoinTablesDBDAO();

	// Private CTOR
	private AdminFacade() {
		System.out.println("Loged in as Admin");
	}

	// Methods
	/**
	 * This is a static method that the singleton class {@link CouponSystem}
	 * uses, through it you login, If your user name and password (the
	 * parameters the method gets) are correct the methods returns a
	 * Facade.</br>
	 * If the parameters are incorrect it will throw a
	 * {@link CouponSystemException}.</br>
	 * On this login the user name & password are always 'admin' / '1234'.
	 * 
	 * @param username
	 *            A String of the user name you want to login with.
	 * @param password
	 *            A String of the user's password you want to login with.
	 * @return On a successful login returns AdminFacade.
	 */
	public static AdminFacade login(String username, String password) throws CouponSystemException {
		if (username.equals("admin") && password.equals("1234")) {
			return new AdminFacade();
		} else {
			throw new CouponSystemException("Failed to login as Admin");
		}
	}

	/**
	 * With this method the Admin user can create a new Company.<br>
	 * <b>Be aware :</b> if this company name already exists it will throw an
	 * {@link CouponSystemException}
	 * 
	 * @param company
	 *            A Company object that you want to create
	 */
	public void createCompany(Company company) throws CouponSystemException {
		// check if that company name doesn't already exists.
		Collection<Company> companies = compdao.getAllCompanies();
		for (Company companyCheck : companies) {
			boolean checkExistence = companyCheck.getCompName().equals(company.getCompName());
			if (checkExistence) {
				throw new CouponSystemException("Company name : " + company.getCompName() + " already exist");
			}
		}
		// if we are here that company name doesn't exists.
		compdao.createCompany(company);
		System.out.println("Created Company " + company.getCompName() + " successfully");
	}

	/**
	 * With this method the Admin user can delete a Company.</br>
	 * <b>Be aware :</b> if you delete a company all the company's coupons & the
	 * customers coupons who bought a coupon from this company will be deleted
	 * as well.
	 * 
	 * @param company
	 *            A company object that you want to delete.
	 */
	public void removeCompany(Company company) throws CouponSystemException {
		// check if this company exists or it will throw exception.
		Company companyFromDB = compdao.getCompany(company.getId());
		/*
		 * checks if the company's name from the parameter we got is match to
		 * the name of the same companies id from the DB and from the parameter.
		 **/
		boolean checkIdName = companyFromDB.getCompName().equals(company.getCompName());
		if (checkIdName) {
			Collection<Coupon> couponsToDele = compdao.getCouppons(company);
			for (Coupon coupon : couponsToDele) {
				// deleting coupons from the Join tables and Coupon table.
				joinTtablesdao.deleteCompanyCoupon(coupon);
				joinTtablesdao.deleteCustomerCoupon(coupon);
				coupdao.removeCoupon(coupon);
			}
			compdao.removeCompany(companyFromDB);
			System.out.println("Deleted Company " + company.getCompName() + " successfully");
		} else {
			throw new CouponSystemException("Company's ID not match to his name !");
		}
	}

	/**
	 * With this method the Admin user can update a specific Company
	 * details.</br>
	 * This method will only update the company's password & email.
	 * 
	 * @param company
	 *            A company object that you want to update.
	 */
	public void updateCompany(Company company) throws CouponSystemException {
		// checks if this company exists or will throw exception
		Company companyDB = compdao.getCompany(company.getId());
		/*
		 * checks if the company's name from the parameter we got is match to
		 * the name of the same companies id from the DB and from the parameter.
		 **/
		boolean checkIdName = companyDB.getCompName().equals(company.getCompName());
		if (checkIdName) {
			companyDB.setPassword(company.getPassword());
			companyDB.setEmail(company.getEmail());
			compdao.updateCompany(companyDB);
			System.out.println("Updated Company " + company.getCompName() + " successfully");
		} else {
			throw new CouponSystemException("ID not match to the Company's name !");
		}
	}

	/**
	 * With this method the Admin user gets a specific Company through the
	 * company's ID that the method gets.
	 * 
	 * @param id
	 *            A long of the company's ID that you want to get.
	 * @return A Company object.
	 */
	public Company getCompany(long id) throws CouponSystemException {
		Company company = compdao.getCompany(id);
		return company;
	}

	/**
	 * With this method the Admin user can get a list of all the companies.</br>
	 * If there are no companies yet the list will be null.
	 * 
	 * @return A Collection list of Company with all the companies in it.
	 */
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Collection<Company> companies = compdao.getAllCompanies();
		return companies;
	}

	/**
	 * With this method the Admin user can create a new Customer.
	 * 
	 * @param customer
	 *            A Customer object that you want to create
	 */
	public void createCustomer(Customer customer) throws CouponSystemException {
		// check if that customer name doesn't already exist.
		Collection<Customer> customers = custdao.getAllCustomers();
		for (Customer customerCheck : customers) {
			boolean checkExistence = customerCheck.getCustName().equals(customer.getCustName());
			if (checkExistence) {
				throw new CouponSystemException("Customer name : " + customer.getCustName() + " already exist");
			}
		}
		// if we are here that customer name doesn't exist.
		custdao.createCustomer(customer);
		System.out.println("Created Customer " + customer.getCustName() + " successfully");
	}

	/**
	 * With this method the Admin user can delete a Customer.</br>
	 * <b>Be aware :</b> if you delete a customer all the customer's coupons
	 * will be deleted as well.
	 * 
	 * @param customer
	 *            A customer object that you want to delete.
	 */
	public void removeCustomer(Customer customer) throws CouponSystemException {
		// checks if that customer exists if not it will throw exception.
		Customer customerFromDB = custdao.getCustomer(customer.getId());
		/*
		 * checks if the customer's name from the parameter we got is match to
		 * the name of the same customers id from the DB and from the parameter.
		 **/
		boolean checkIdName = customerFromDB.getCustName().equals(customer.getCustName());
		if (checkIdName) {
			// gets all the coupons of that customer.
			Collection<Coupon> couponsToDele = custdao.getCoupons(customer);
			for (Coupon coupon : couponsToDele) {
				// deleting coupons from the Join table of that customer.
				joinTtablesdao.deleteCustomerCoupon(coupon);
			}
			custdao.removeCustomer(customerFromDB);
			System.out.println("Deleted Customer " + customer.getCustName() + " successfully");
		} else {
			throw new CouponSystemException("ID not match to this customer's name !");
		}
	}

	/**
	 * With this method the Admin user can update a specific Customer
	 * details.</br>
	 * This method will only update the customer's password.
	 * 
	 * @param customer
	 *            A customer object that you want to update.
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {
		// checks if this customer exists or will throw exception.
		Customer customerDB = custdao.getCustomer(customer.getId());
		/*
		 * checks if the customer's name from the parameter we got is match to
		 * the name of the same customers id from the DB and from the parameter.
		 **/
		boolean checkIdName = customerDB.getCustName().equals(customer.getCustName());
		if (checkIdName) {
			customerDB.setPassword(customer.getPassword());
			custdao.updateCustomer(customerDB);
			System.out.println("Updated Customer " + customer.getCustName() + " successfully");
		} else {
			throw new CouponSystemException("ID not match to this customer's name !");
		}
	}

	/**
	 * With this method the Admin user gets a specific Customer through the
	 * customer's ID that the method gets.
	 * 
	 * @param id
	 *            A long of the customer's ID that you want to get.
	 * @return A Customer object.
	 */
	public Customer getCustomer(long id) throws CouponSystemException {
		Customer customer = custdao.getCustomer(id);
		return customer;
	}

	/**
	 * With this method the Admin user can get a list of all the customers.</br>
	 * If there are no customers yet the list will be null.
	 * 
	 * @return A Collection list of Customer with all the customers in it.
	 */
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		Collection<Customer> customers = custdao.getAllCustomers();
		return customers;
	}

	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		Collection<Coupon> coupons = coupdao.getAllCoupons();
		return coupons;
	}

}
