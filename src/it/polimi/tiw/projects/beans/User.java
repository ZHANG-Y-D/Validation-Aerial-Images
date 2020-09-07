package it.polimi.tiw.projects.beans;

public class User {
	
	private String role;
	private String username;
	private String email;

	private String level = null;
	private String foto = null;


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

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
