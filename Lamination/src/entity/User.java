

package entity;

import java.io.Serializable;
import javax.persistence.*;


import java.util.Date;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String active;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLastPassword;

	private String emailAddress;

	private String fullName;

	private String password;

	private String phoneNumber;

	private String userName;

	private String userType;
	
	private String pais;

	public User() {
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "active")
	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	@Column(name = "dateLastPassword")
	public Date getDateLastPassword() {
		return this.dateLastPassword;
	}
	
	public void setDateLastPassword(Date dateLastPassword) {
		this.dateLastPassword = new Date(dateLastPassword.getTime());
	}
	@Column(name = "emailAddress")
	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	@Column(name = "fullName")
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name = "phoneNumber")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Column(name = "userName")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "userType")
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Column(name = "pais")
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
}