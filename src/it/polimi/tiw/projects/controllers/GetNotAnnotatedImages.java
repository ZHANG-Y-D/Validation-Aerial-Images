//package it.polimi.tiw.projects.controllers;
//
//import com.google.gson.Gson;
//import it.polimi.tiw.projects.beans.Image;
//import it.polimi.tiw.projects.beans.User;
//import it.polimi.tiw.projects.dao.ImageDAO;
//import it.polimi.tiw.projects.dao.WorkerDAO;
//import it.polimi.tiw.projects.utils.ConnectionHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//@WebServlet("/GetNotAnnotatedImages")
//public class GetNotAnnotatedImages extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    private Connection connection;
//
//    public GetNotAnnotatedImages() {
//        super();
//    }
//
//    @Override
//    public void init() throws ServletException {
//
//        connection = ConnectionHandler.getConnection(getServletContext());
//
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        User user = null;
//        List<Image> images;
//        WorkerDAO workerDAO = null;
//        List<String> campaignListSubScribe = null;
//        List<String> campaignListNotSubScribe = null;
//
//        HttpSession session = request.getSession(false);
//
//
//        try {
//            if (session == null || session.getAttribute("user") == null) {
//                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                response.getWriter().println("Can't find user or campaign");
//                return;
//            }
//            user = (User) session.getAttribute("user");
//            if(user == null){
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().println("Invalid access");
//                return;
//            }
//            workerDAO = new WorkerDAO(connection, user.getUsername());
//
//            String campagnaName = request.getParameter("CampaignName");
//            session.setAttribute("CampaignName", campagnaName);
//
//            campaignListSubScribe = (List<String>) session.getAttribute("subscribedCampaignsName");
//            campaignListNotSubScribe = (List<String>) session.getAttribute("notSubscribedCampaignsName");
//
//            if (session.getAttribute("notAnnotatedImages") == null) {
//
//                    if (!campaignListNotSubScribe.contains(campagnaName) && !campaignListSubScribe.contains(campagnaName)) {
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        response.getWriter().println("Campaign not valid");
//                        return;
//                    }
//                    if (!workerDAO.isStarted(campagnaName)) {
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        response.getWriter().println("Campaign is closed");
//                        return;
//                    }
//
//                    images = workerDAO.notAnnotatedImage(campagnaName);
//                    session.setAttribute("notAnnotatedImages", images);
//                    String json = new Gson().toJson(images);
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    response.getWriter().write(json);
//
//            } else {
//
//                    images = (List<Image>) session.getAttribute("notAnnotatedImages");
//                    String json = new Gson().toJson(images);
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    response.getWriter().write(json);
//
//            }
//        } catch (IOException | SQLException e) {
//            try {
//                response.sendError(500, "Database access failed");
//            } catch (IOException ignore) {
//
//            }
//        }
//    }
//
//
//    @Override
//    public void destroy() {
//        try {
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException ignore) {
//
//        }
//    }
//
//
//}


package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Image;
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

        User user = null;
        List<Image> images;
        WorkerDAO workerDAO = null;
        List<String> campaignListSubScribe = null;
        List<String> campaignListNotSubScribe = null;

        HttpSession session = request.getSession(false);


        try {
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find user or campaign");
                return;
            }
            user = (User) session.getAttribute("user");
            if(user == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Invalid access");
                return;
            }
            workerDAO = new WorkerDAO(connection, user.getUsername());

            String campagnaName = request.getParameter("CampaignName");
            session.setAttribute("CampaignName", campagnaName);

            campaignListSubScribe = (List<String>) session.getAttribute("subscribedCampaignsName");
            campaignListNotSubScribe = (List<String>) session.getAttribute("notSubscribedCampaignsName");

            /*    if (session.getAttribute("notAnnotatedImages") == null) { */

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
            session.setAttribute("notAnnotatedImages", images);
            String json = new Gson().toJson(images);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

         /*   } else {

                    images = (List<Image>) session.getAttribute("notAnnotatedImages");
                    String json = new Gson().toJson(images);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);

            } */
        } catch (IOException | SQLException e) {
            try {
                response.sendError(500, "Database access failed");
            } catch (IOException ignore) {

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