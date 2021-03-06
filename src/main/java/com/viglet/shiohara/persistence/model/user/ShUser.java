package com.viglet.shiohara.persistence.model.user;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viglet.shiohara.utils.MD5Util;

import java.util.Date;

/**
 * The persistent class for the ShUser database table.
 * 
 */
@Entity
@NamedQuery(name = "ShUser.findAll", query = "SELECT s FROM ShUser s")
public class ShUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username")
	private String username;

	private String confirmEmail;

	@Column(name = "email")
	private String email;

	private String firstName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	private String lastName;

	private String lastPostType;

	private int loginTimes;

	@Column(name = "password")
	private String password;

	private String realm;

	private String recoverPassword;

	@Column(name = "enabled")
	private int enabled;

	public ShUser() {

	}

	public ShUser(ShUser shUser) {
		this.username = shUser.username;
		this.email = shUser.email;
		this.password = shUser.password;
		this.enabled = shUser.enabled;
	}

	public String getConfirmEmail() {
		return this.confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastPostType() {
		return this.lastPostType;
	}

	public void setLastPostType(String lastPostType) {
		this.lastPostType = lastPostType;
	}

	public int getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealm() {
		return this.realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getRecoverPassword() {
		return this.recoverPassword;
	}

	public void setRecoverPassword(String recoverPassword) {
		this.recoverPassword = recoverPassword;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("gravatar")
	private String getGravatar() {
		if (this.email != null) {
			String imageUrl = "https://www.gravatar.com/avatar/" + MD5Util.md5Hex(this.email);
			return imageUrl;
		} else {
			return null;
		}
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

}
