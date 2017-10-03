package core.test;

import java.util.Collection;
import java.util.Date;
import core.CouponSystem;
import core.beans.Coupon;
import core.beans.CouponType;
import core.exception.CouponSystemException;
import core.facade.CompanyFacade;
import core.facade.clientType;

public class CompanyTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		Date endDate = new Date(117, 8, 18);

		Coupon coupon1 = new Coupon(301, "Dinner", new Date(), endDate, 2, CouponType.FOOD, "message", 5.9, "image");
		Coupon coupon2 = new Coupon(302, "SkyJump", new Date(), endDate, 2, CouponType.SPORTS, "message", 8.0, "image");
		CompanyFacade company = null;

		try {
			company = (CompanyFacade) CouponSystem.getInstance().login("IBM", "1234", clientType.COMPANY);

			System.out.println("========Create========");
			company.createCoupon(coupon1);
			company.createCoupon(coupon2);
			System.out.println("========Update========");
			coupon1.setPrice(8);
			company.updateCoupon(coupon1);
			System.out.println("========getAll========");
			Collection<Coupon> allCoupons = company.getAllCompanyCoupons();
			System.out.println(allCoupons);
			Collection<Coupon> allCouponsByType = company.getAllCompanyCouponsByType(CouponType.FOOD);
			System.out.println(allCouponsByType);
			Collection<Coupon> allCouponsByPrice = company.getAllCompanyCouponsByPrice(8);
			System.out.println(allCouponsByPrice);
			System.out.println("========getSpecific========");
			Coupon coupon = company.getCoupon((long) 301);
			System.out.println(coupon);
			System.out.println("========Remove========");
			company.removeCoupon(coupon2);

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
