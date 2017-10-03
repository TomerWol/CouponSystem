package core.test;

import java.util.Collection;

import core.CouponSystem;
import core.beans.Company;
import core.beans.Coupon;
import core.beans.Customer;
import core.exception.CouponSystemException;
import core.facade.AdminFacade;
import core.facade.clientType;
import db.connection_pool.ConnectionPool;

public class AdminTest {

	public static void main(String[] args) {

		Company company1 = new Company((long) 101, "IBM", "1111", "IBM@gmail.com");
		Company company2 = new Company((long) 102, "Apple", "2222", "apple@gmail.com");
		Customer customer1 = new Customer(201, "tomer", "1111");
		Customer customer2 = new Customer(202, "eldar", "2222");
		AdminFacade admin = null;
		ConnectionPool.getInstance();
		try {
			admin = (AdminFacade) CouponSystem.getInstance().login("admin", "1234", clientType.ADMIN);
			
			System.out.println("========Create========");
			admin.createCompany(company1);
			admin.createCompany(company2);
			admin.createCustomer(customer1);
			admin.createCustomer(customer2);
			System.out.println("========Update========");
			company1.setPassword("2222");
			admin.updateCompany(company1);
			customer1.setPassword("2222");
			admin.updateCustomer(customer1);
			System.out.println("========getAll========");
			Collection<Company> allCompanies = admin.getAllCompanies();
			System.out.println(allCompanies);
			Collection<Customer> allCustomers = admin.getAllCustomers();
			System.out.println(allCustomers);
			Collection<Coupon> allCoupons = admin.getAllCoupons();
			System.out.println(allCoupons);
			System.out.println("========getSpecific========");
			Company company = admin.getCompany(101);
			System.out.println(company);
			Customer customer = admin.getCustomer(201);
			System.out.println(customer);
			System.out.println("========Remove========");
			admin.removeCompany(company2);
			admin.removeCustomer(customer2);
			
			
			
			
			
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				CouponSystem.getInstance().shutDown();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
