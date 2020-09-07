package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.ManagerDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;


@WebServlet("/GetCampaignList")
public class GetCampaignList extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	
	public GetCampaignList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u;
		HttpSession s = request.getSession();
		u = (User) s.getAttribute("user");
        ManagerDAO m = new ManagerDAO(connection,u.getUsername());

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Can't access this page");
			return;
		}


		List<Campaign> campaigns;
		
		try {
			campaigns = m.findCampaigns();
			
		}catch(SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover campaigns");
			return;
		}
		
		String json = new Gson().toJson(campaigns);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
		
	}


	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
