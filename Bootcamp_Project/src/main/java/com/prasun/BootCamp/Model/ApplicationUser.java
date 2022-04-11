package com.prasun.BootCamp.Model;

import java.util.Collection;
import java.util.Set;




import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="user")
public class ApplicationUser extends Auditable<String> implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Email(message = "Enter Valid Email Address!")
//	@Column(unique = true)
//	@NotNull
	private String email;
	//@NotNull(message = "Mandatorry Field !")

	private String firstName;

	private String middleName;
	//@NotNull(message = "Mandatory Field!")
	private String lastName;
	//@NotNull

	private String password;
//	@Column(unique = true)
//	@NotNull(message = "Enter a valid Email Address")
//	@Size(min = 10,max = 10)
	private String phoneNumber;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	private boolean isActive;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Address address;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ApplicationUser(String email, String password, Set<Role> roles) {
	}

	public ApplicationUser(String email, String firstName, String middleName, String lastName, String password, String phoneNumber, Set<Role> roles) {
		this.email = email;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}

	public ApplicationUser() {
	}
	/*
	 * ID EMAIL FIRST_NAME MIDDLE_NAME LAST_NAME PASSWORD IS_DELETED IS_ACTIVE
	 * IS_EXPIRED IS_LOCKED INVALID_ATTEMPT_COUNT PASSWORD_UPDATE_DATE
	 */
}
