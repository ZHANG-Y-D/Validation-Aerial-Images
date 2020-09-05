package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		pstatement.setString(1, name);
		result = pstatement.executeQuery();

		while (result.next()){
			String campaign = result.getString("CampagnaName");
			campaigns.add(campaign);

		}
		return campaigns;

	}

	public List<String> getNotSubscribeCampaigns() throws  SQLException{
		List<String> campaigns = new ArrayList<String>();
		String query = "SELECT Name FROM campagna WHERE stato = 1 AND Name NOT IN SELECT distinct i.campagnaName FROM iscrizioni AS i right join campagna AS c  ON i.CampagnaName = c.Name WHERE i.LavoratoreName = ? AND c.Stato = 1;";

		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		pstatement.setString(1, name);
		result = pstatement.executeQuery();

		while (result.next()){
			String campaign = result.getString("Name");
			campaigns.add(campaign);

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


}
