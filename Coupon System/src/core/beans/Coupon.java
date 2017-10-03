package core.beans;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * This class contains all the attributes that represents a Coupon.</br>
 * Attributes such as : id, title, Start Date, End Date, amount, CouponType, message, price, image.
 */
@XmlRootElement
public class Coupon implements Serializable {
	private static final long serialVersionUID = 1L;
	// Attributes
	private long id; //represents the Primary Key in the Coupon, Coustomer_Coupon & Company_Coupon tables.
	private String title;// the coupon's title.
	private Date startDate;// the coupon's start date.
	private Date endDate;// the coupon's end date.
	private int amount;// the coupon's amount.
	private CouponType type;// the coupon's type.
	private String message;// the coupon's message.
	private double price;// the coupon's price.
	private String image;// the coupons image.

	// COTRs
	public Coupon() {
	}

	public Coupon(long id, String title, Date startDate, Date endDate, int amount, CouponType type, String message,
			double price, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}

	// Getters & Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image="
				+ image + "]";
	}

}
