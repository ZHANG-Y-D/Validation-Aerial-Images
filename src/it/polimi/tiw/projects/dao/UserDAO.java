package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import it.polimi.tiw.projects.beans.User;


public class UserDAO {
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String usrn, String pwd) throws SQLException {
		String query = "SELECT  Name,Ruolo FROM utente  WHERE Name = ? AND Password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setUsername(result.getString("Name"));
					user.setRole(result.getString("Ruolo"));
					//user.setEmail(result.getString("email"));
					return user;
				}
			}
		}
	}
	
	public String insertForRegister(String usrn, String pwd, String email) throws SQLException {
		String query = "INSERT INTO user(id,username,password,email) VALUES(NULL,?,?,?);";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			pstatement.setString(3, email);
			pstatement.executeUpdate();
			return "OK";
		} catch (SQLException e) {
				return e.getMessage();
		}
	}


	//todo
	public User GetUserInformation(String name) throws SQLException{

		String query = "SELECT * FROM utente WHERE Name = ?";
		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		pstatement.setString(1, name);
		result = pstatement.executeQuery();

		if (!result.isBeforeFirst()) // no results
			return null;
		else {
			User user = new User();
			result.next();
			user.setUsername(name);
			user.setRole(result.getString("Ruolo"));
			user.setEmail(result.getString("Email"));

			if(result.getString("Ruolo") == "Lavoratore"){
				user.setLevel(result.getString("LavoratoreLevel"));
				user.setFoto(Base64.getEncoder().encodeToString(result.getBytes("LavoratoreFoto")));

			}

			return user;
		}


	}

	public boolean existsWorker(String name) throws SQLException{

		String query = "SELECT COUNT(DISTINCT Name) AS Count FROM utente WHERE Name = ? AND Ruolo = \"Lavoratore\" ";
		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		pstatement.setString(1, name);
		result = pstatement.executeQuery();

		result.next();
		if(result.getInt("Count") > 0){
			return true;
		}else return  false;

	}
	
	
}
