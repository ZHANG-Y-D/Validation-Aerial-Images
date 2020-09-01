package it.polimi.tiw.projects.dao;

import java.sql.Connection;

public class ImageDAO {
        
	private Connection con;
	

	public ImageDAO(Connection connection) {
		this.con = connection;
	}
}
