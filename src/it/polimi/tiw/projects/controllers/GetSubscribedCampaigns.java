package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetSubscribedCampaigns")
public class GetSubscribedCampaigns extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public GetSubscribedCampaigns() {
        super();
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        User user = (User)s.getAttribute("user");
        List<String> campaignsName ;

        WorkerDAO workerDAO = new WorkerDAO(connection,user.getUsername());

        try {
            campaignsName = workerDAO.getSubscribeCampaigns();
            s.setAttribute("subscribedCampaignsName", campaignsName);

        }catch (SQLException e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Not possible to recover worker's campaign");
            return;
        }

        String json = new Gson().toJson(campaignsName);
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
