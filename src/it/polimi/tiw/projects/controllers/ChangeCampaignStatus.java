package it.polimi.tiw.projects.controllers;

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

@WebServlet("/ChangeCampaignStatus")
public class ChangeCampaignStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public ChangeCampaignStatus() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int status = -1;
        String campagnaName = null;
        CampaignDAO campaignDAO = null;

        try {
            status = Integer.parseInt(request.getParameter("Status"));
            if (status == -1){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Campaign status error!");
                return;
            }

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CampaignName") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }
            campagnaName = (String) session.getAttribute("CampaignName");
            campaignDAO = new CampaignDAO(connection,campagnaName);
            String result = campaignDAO.changeCampaignStatus(status);

            if (result.contains("OK")){
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println(result);
            }

        } catch (NumberFormatException e){
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Campaign status error!");
            } catch (IOException ignore){

            }
        } catch (IOException ignore){

        }

    }

    @Override
    public void destroy() {
    		try {
    			ConnectionHandler.closeConnection(connection);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
}