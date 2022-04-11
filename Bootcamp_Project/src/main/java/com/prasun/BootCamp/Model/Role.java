package com.prasun.BootCamp.Model;

import java.util.Set;

import javax.persistence.*;

import com.prasun.BootCamp.Enums.Role_Enum;
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
     @ManyToMany(mappedBy = "roles")
	private Set<ApplicationUser> users;

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
