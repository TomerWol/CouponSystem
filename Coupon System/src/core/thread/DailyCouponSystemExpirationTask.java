package core.thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import core.beans.Coupon;
import core.dao.db.CouponDBDAO;
import core.dao.db.JoinTablesDBDAO;
import core.exception.CouponSystemException;

/**
 * A runnable class which contain a daily process of removing expired coupons
 * from the DB.</br>
 * After he finishes the process will go to sleep for 24 Hours and then will
 * process all over again until the system shuts down.
 */
public class DailyCouponSystemExpirationTask implements Runnable {
	// Attributes
	private static CouponDBDAO coupdao = new CouponDBDAO();
	private static JoinTablesDBDAO tablesdao = new JoinTablesDBDAO();
	private Date todaysDate = null;
	private boolean run = true;

	// CTOR
	public DailyCouponSystemExpirationTask() {
	}

	/**
	 * a running method that removing expired coupons from the DB and go to
	 * sleep for 24 Hours and then start again until the system shuts down.
	 */
	@Override
	public void run() {

		while (run) {
			todaysDate = Calendar.getInstance().getTime();
			Collection<Coupon> coupons = new ArrayList<>();
			try {
				coupons = coupdao.getAllCoupons();
				for (Coupon coupon : coupons) {
					if (todaysDate.after(coupon.getEndDate())) {
						coupdao.removeCoupon(coupon);
						tablesdao.deleteCompanyCoupon(coupon);
						tablesdao.deleteCustomerCoupon(coupon);
					}
				}
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (CouponSystemException | InterruptedException e) {
				System.out.println(e.getMessage() + " (shutting down)");
				run = false;

			}
		}
	}

}
