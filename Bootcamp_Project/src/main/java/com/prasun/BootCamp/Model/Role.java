package com.prasun.BootCamp.Model;

import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;


@Entity
public class Role implements GrantedAuthority{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Override
	public String getAuthority( ) {

		return name;
	}
}
