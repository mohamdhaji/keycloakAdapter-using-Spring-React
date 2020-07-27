package com.dress.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(updatable = false, unique = true)
	private String userIdentifier;
	private String userName;
	@Email(message = "not valid email")
	@NotBlank(message = "email is required")
	@Column(unique = true)
	private String emailAddress;
	@NotBlank(message = "Please enter your first name")
	private String firstName;
	@NotBlank(message = "Please enter your last name")
	private String lastName;
	@NotBlank(message = "Password field is required")
	@Transient
	private String password;
	@Transient
	@NotBlank(message = "confirm password is required")
	private String confirmPassword;
	private Date createAt;
	private Date updateAt;
	//OneToOne with cart
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
	private Cart cart;

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public User()

	{

	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	@PrePersist
	protected void onCreate(){
		this.createAt = new Date();
	}

	@PreUpdate
	protected void onUpdate(){
		this.updateAt = new Date();
	}
}
