package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.WorkerDAO;
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
import java.util.List;

@WebServlet("/subscribeToCampaign")
public class SubscribeToCampaign extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public SubscribeToCampaign() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String campaign = request.getParameter("campaignName");
        HttpSession s = request.getSession();
        List<String> notSubscribedList = (List<String>)s.getAttribute("notSubscribedCampaignsName");
        User u = (User) s.getAttribute("user");
        String resultString = null;

        try {
            if(notSubscribedList.contains(campaign)){
                WorkerDAO workerDAO = new WorkerDAO(connection,u.getUsername());
                if(!workerDAO.isStarted(campaign)){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("The campaign is closed");
                    return;
                }
                resultString = workerDAO.subscribeToCampaign(campaign);
                if (resultString.contains("OK")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                }else {
                    response.getWriter().println("campaign not valid");
                }
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("The campaign not valid");
            }
        }catch (SQLException e){
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error, retry later. "+e.getMessage());
            } catch (IOException ignore){

            }
        }catch (IOException ignore){

        }
    }

}
