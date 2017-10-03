package core.test;

import java.util.Collection;
import java.util.Date;
import core.CouponSystem;
import core.beans.Coupon;
import core.beans.CouponType;
import core.exception.CouponSystemException;
import core.facade.CustomerFacade;
import core.facade.clientType;

public class CustomerTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		Date endDate = new Date(117, 8, 18);

		Coupon coupon1 = new Coupon(301, "Dinner", new Date(), endDate, 2, CouponType.FOOD, "message", 8, "image");
		CustomerFacade customer = null;

		try {
			customer = (CustomerFacade) CouponSystem.getInstance().login("tomer", "2222", clientType.CUSTOMER);

			System.out.println("========Purchase========");
			customer.purchaseCoupon(coupon1);
			System.out.println("========GetAll========");//will be the same coupon.
			Collection<Coupon> allCoupons = customer.getCustomerCoupons();
			System.out.println(allCoupons);
			Collection<Coupon> allCouponsByPrice = customer.getCustomerCouponsByPrice(8);
			System.out.println(allCouponsByPrice);
			Collection<Coupon> allCouponsByType = customer.getCustomerCouponsByType(CouponType.FOOD);
			System.out.println(allCouponsByType);

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
