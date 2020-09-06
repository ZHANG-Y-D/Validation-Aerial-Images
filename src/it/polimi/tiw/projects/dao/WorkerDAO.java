package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {
	private Connection con;
	private String name;
	
	public WorkerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}


	public List<String> getSubscribeCampaigns() throws SQLException{
		List<String> campaigns = new ArrayList<String>();

		String query = "SELECT distinct i.campagnaName FROM iscrizione AS i left join campagna AS c  ON i.CampagnaName = c.Name WHERE i.LavoratoreName = ? AND c.Stato = 1;";
		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		try {
			pstatement.setString(1, name);
			result = pstatement.executeQuery();

			while (result.next()){
				String campaign = result.getString("CampagnaName");
				campaigns.add(campaign);

			}
		}catch (SQLException e){
			throw new SQLException(e);
		}

		return campaigns;

	}

	public List<String> getNotSubscribeCampaigns() throws  SQLException{
		List<String> campaigns = new ArrayList<String>();
		String query = "SELECT Name FROM campagna WHERE stato = 1 AND Name NOT IN (SELECT distinct i.CampagnaName FROM iscrizione AS i right join campagna AS c  ON i.CampagnaName = c.Name WHERE i.LavoratoreName = ? AND c.Stato = 1);";

		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		try {
			pstatement.setString(1, name);
			result = pstatement.executeQuery();

			while (result.next()){
				String campaign = result.getString("Name");
				campaigns.add(campaign);

			}
		}catch (SQLException e){
			throw new SQLException(e);
		}

		return campaigns;
	}

	public String subscribeToCampaign(String campaign) throws SQLException{
		String query = "INSERT INTO iscrizione(LavoratoreName, CampagnaName) VALUES(?,?);";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, name);
			pstatement.setString(2, campaign);
			pstatement.executeUpdate();
			return "OK";
		} catch (SQLException e) {
			return e.getMessage();
		}

	}

	public boolean isStarted(String campaign) throws SQLException{
		String query = "SELECT Stato FROM campagna WHERE Name = ?";

		ResultSet result = null;
		PreparedStatement pstatement = null;
		pstatement = con.prepareStatement(query);

		try {
			pstatement.setString(1, campaign);
			result = pstatement.executeQuery();
			result.next();

			if(result.getInt("Stato") == 1){
				return true;
			}else return false;


		} catch (SQLException e) {
			throw new SQLException(e);
		}finally {
			if(result!= null){
				result.close();
			}
		}

	}


}
