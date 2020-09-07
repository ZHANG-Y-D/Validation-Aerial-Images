package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Annotation;
import it.polimi.tiw.projects.beans.Image;
import it.polimi.tiw.projects.dao.ImageDAO;
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


@WebServlet("/GetAnnotations")
public class GetAnnotations extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public GetAnnotations() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("CampaignName") == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.getWriter().println("Can't find campaign name");
            return;
        }
        String campagnaName = (String) session.getAttribute("CampaignName");

       int imageId = Integer.parseInt(request.getParameter("imageID"));
       ImageDAO imageDAO = new ImageDAO(connection);

        List<Annotation> annotationList;

        try {
            if(imageDAO.existsImageId(imageId,campagnaName)){
                annotationList = imageDAO.getAnnotationsByImageId(imageId);
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Image's id doesn't exist");
                return;
            }

        }catch (SQLException e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Not possible to recover annotations");
            return;
        }

        String json = new Gson().toJson(annotationList);
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
