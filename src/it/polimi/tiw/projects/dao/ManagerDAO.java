package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.CampaignStatus;


public class ManagerDAO {
	
	private Connection con;
	private String name;
	
	public ManagerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}
	
	
	public List<Campaign> findCampaigns() throws SQLException {
		
		List<Campaign> campaigns = new ArrayList<Campaign>();
		String query =  "SELECT Name FROM campagna WHERE ManagerName = ? ORDER BY Name ASC";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, this.name);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Campaign campaign= new Campaign();
					campaign.setName(result.getString("Name"));
					campaigns.add(campaign);
				}
			}
		}
		
		return campaigns;
	}
	
	
	public String createCampaign(String name, String client) throws SQLException{
		String query = "INSERT into campagna (Name, Committente, Stato,ManagerName)   VALUES(?, ?, ?,?)";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, name);
			pstatement.setString(2, client);
			pstatement.setInt(3, CampaignStatus.CREATED.getValue());
			pstatement.setString(4, this.name);
			pstatement.executeUpdate();
			return "OK";
		}catch (SQLException e) {
			return e.getMessage();
		}
		
	}

}
