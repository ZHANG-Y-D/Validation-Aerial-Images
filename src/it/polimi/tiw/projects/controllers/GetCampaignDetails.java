package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.dao.CampaignDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/GetCampaignDetails")
public class GetCampaignDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public GetCampaignDetails() {
        super();
    }

    @Override
    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String campaignName = request.getParameter("CampaignName");
        CampaignDAO campaignDAO = new CampaignDAO(connection, campaignName);
        Campaign campaign = null;


        HttpSession session = request.getSession();
        session.setAttribute("CampaignName", campaignName);

        try {
            campaign = campaignDAO.getCampaignDetails();
            String json = new Gson().toJson(campaign);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            try {
                response.sendError(500, "Database access failed");
            }catch (IOException ignore){

            }
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignore) {

        }
    }
}
