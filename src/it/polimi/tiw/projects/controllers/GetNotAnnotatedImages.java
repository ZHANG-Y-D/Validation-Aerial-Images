package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Image;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.ImageDAO;
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

@WebServlet("/GetNotAnnotatedImages")
public class GetNotAnnotatedImages extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public GetNotAnnotatedImages() {
        super();
    }

    @Override
    public void init() throws ServletException {

        connection = ConnectionHandler.getConnection(getServletContext());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User u;
        HttpSession s = request.getSession();
        u = (User) s.getAttribute("user");
        WorkerDAO workerDAO = new WorkerDAO(connection, u.getUsername());
        List<Image> images;
        String campagnaName = request.getParameter("CampaignName");
        List<String> campaignListSubScribe = (List<String>) s.getAttribute("subscribedCampaignsName");
        List<String> campaignListNotSubScribe = (List<String>) s.getAttribute("notSubscribedCampaignsName");
        s.setAttribute("campaignName",campagnaName);

        if (s.getAttribute("notAnnotatedImages") == null) {
            try {
                if (!campaignListNotSubScribe.contains(campagnaName) && !campaignListSubScribe.contains(campagnaName)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Campaign not valid");
                    return;
                }
                if (!workerDAO.isStarted(campagnaName)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Campaign is closed");
                    return;
                }

                images = workerDAO.notAnnotatedImage(campagnaName);
                s.setAttribute("notAnnotatedImages", images);
                String json = new Gson().toJson(images);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } catch (IOException | SQLException e) {
                try {
                    response.sendError(500, "Database access failed");
                } catch (IOException ignore) {

                }
            }
        } else {
            try {
                images = (List<Image>) s.getAttribute("notAnnotatedImages");
                String json = new Gson().toJson(images);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } catch (IOException e) {
                response.sendError(500, e.getMessage());

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
