package core.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains all the attributes that represents a Customer.</br>
 * Attributes such as : id, name, password.
 */
@XmlRootElement
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	// Attributes
	private long id;// represents the Primary Key in the Customer & Customer_Coupon Tables.
	private String custName;// the customer's name.
	private String password;// the customers's password.

	// COTRs
	public Customer() {
	}

	public Customer(long id, String custName, String password) {
		super();
		this.id = id;
		this.custName = custName;
		this.password = password;
	}

	// Getters & Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custName + ", password=" + password + "]";
	}

}
