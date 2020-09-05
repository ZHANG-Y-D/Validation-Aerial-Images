package it.polimi.tiw.projects.beans;

public class User {
	
	private String role;
	private String username;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}
	

	public void setRole(String r) {
		role = r;
	}

	public void setUsername(String un) {
		username = un;
	}

}
