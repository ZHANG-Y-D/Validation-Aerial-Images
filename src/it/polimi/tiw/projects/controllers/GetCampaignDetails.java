package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.CampaignDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
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
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

        CampaignDAO campaignDAO =  null;
        Campaign campaign = null;

        String campaignName = request.getParameter("CampaignName");
        HttpSession session = request.getSession(false);

        try {
            campaignDAO =  new CampaignDAO(connection, campaignName);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find user");
                return;
            }
            User user = (User) session.getAttribute("user");
            if(!campaignDAO.getCampaignDetails().getManager().equals(user.getUsername())){
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println("This campaign does not belong to you");
                return;
            }

            if(campaignName == null){
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }

            session.setAttribute("CampaignName", campaignName);


            campaign = campaignDAO.getCampaignDetails();
            String json = new Gson().toJson(campaign);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException | SQLException e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Database access failed");
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
