package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Campaign;


public class ManagerDAO {
	
	private Connection con;
	private String name;
	
	public ManagerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}
	
	
	public List<Campaign> findCampaigns() throws SQLException {
		
		List<Campaign> campaigns = new ArrayList<Campaign>();
		String query =  "SELECT name FROM campagna WHERE ManagerName = ? ORDER BY Name ASC";
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

}
