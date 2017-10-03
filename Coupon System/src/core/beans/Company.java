package core.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains all the attributes that represents a Company.<br>
 * Attributes such as : id, name, password, email.
 */
@XmlRootElement
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;
	// Attributes
	private Long id; // represents the Primary Key in the Company & Company_Coupon Tables.
	private String compName; // the company's name.
	private String password; // the company's password.
	private String email; // the company's email.

	// COTRs
	public Company() {
	}
	
	public Company(Long id, String compName, String password, String email) {
		super();
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email + "]";
	}

}
