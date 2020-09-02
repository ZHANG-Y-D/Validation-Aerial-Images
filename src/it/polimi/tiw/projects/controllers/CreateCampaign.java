package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.ManagerDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/CreateCampaign")
@MultipartConfig
public class CreateCampaign extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CreateCampaign() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String name = null;
		String clientName = null;
		
		
		name = StringEscapeUtils.escapeJava(request.getParameter("name"));
		clientName = StringEscapeUtils.escapeJava(request.getParameter("client"));
		
		if (name == null || clientName == null || name.isEmpty() || clientName.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Credentials must be not null");
			return;
		}
		
		User u = null;
		HttpSession s = request.getSession();
		u = (User) s.getAttribute("user");
		ManagerDAO manager = new ManagerDAO(connection,u.getUsername());
		
		String resultString = null;
		
		try {
			resultString = manager.createCampaign(name, clientName);
			if (resultString.contains("OK")) {
				response.setStatus(HttpServletResponse.SC_OK);
			}else {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			if (resultString.contains("Duplicate")) { 
				// Duplicate entry. The name has been registered.
		     	response.getWriter().println("The campaign's name already existed, choose another name ");
	
			}else {
				response.getWriter().println(resultString);
			   }
			}
		}catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later. "+e.getMessage());
			return;
		}
	
	
	}

	

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
