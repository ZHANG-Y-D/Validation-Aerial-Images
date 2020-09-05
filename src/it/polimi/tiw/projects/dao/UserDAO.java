package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.beans.Worker;


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

	public User GetUserInformation(String name) throws SQLException{

		String query = "SELECT * WHERE Name = ?";
		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		pstatement.setString(1, name);
		result = pstatement.executeQuery();

		if (!result.isBeforeFirst()) // no results
			return null;
		else {

			if(result.getString("Ruolo") == "Manager"){
				User user = new User();
				user.setUsername(name);
				user.setRole(result.getString("Ruolo"));
				user.setEmail(result.getString("Email"));
				return user;
			}else{
				Worker worker = new Worker();
				worker.setUsername(name);
				worker.setRole(result.getString("Ruolo"));
				worker.setEmail(result.getString("Email"));
				worker.setLevel(result.getString("LavoratoreLevel"));
				worker.setFoto(Base64.getEncoder().encodeToString(result.getBytes("LavoratoreFoto")));
				return worker;
			}


		}


	}
	
	
}
