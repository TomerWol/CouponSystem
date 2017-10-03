package core.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a enum class that represents Coupon types that you can buy -
 * RESTURANS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING.
 */
@XmlRootElement
public enum CouponType implements Serializable {

	RESTURANS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING;

	public String getType() {
		return this.name();
	}

}
