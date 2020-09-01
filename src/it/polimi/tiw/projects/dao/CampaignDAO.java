package it.polimi.tiw.projects.dao;

import java.sql.Connection;

public class CampaignDAO {
	
	private Connection con;
	private String name;
	
	public CampaignDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}

}
