package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerDAO {
	private Connection con;
	private String name;
	
	public WorkerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}




}
