package it.polimi.tiw.projects.dao;

import java.sql.Connection;

public class WorkerDAO {
	private Connection con;
	private String name;
	
	public WorkerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}


}
