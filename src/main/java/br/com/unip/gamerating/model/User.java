package br.com.unip.gamerating.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {

	@Id
	@NotEmpty
	private String username;
	
	@NotBlank
	@Column(name = "password")
	private String password;
	
	@NotBlank
	@Column(name = "name")
	private String name;
	
	public User() {

	}
	
	public User(@NotEmpty String username, @NotBlank String password, @NotBlank String name) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}