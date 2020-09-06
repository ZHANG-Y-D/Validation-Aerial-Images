package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
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

import static java.lang.Integer.parseInt;

@WebServlet("/GetImageDetails")
public class GetImageDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public GetImageDetails() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String campagnaName = null;
        int imageId = -1;
        Image image = null;
        ImageDAO imageDAO = null;

        try {

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CampaignName") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }
            campagnaName = (String) session.getAttribute("CampaignName");

            imageId = Integer.parseInt(request.getParameter("imageId"));
            if(imageId == -1){
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }

            imageDAO = new ImageDAO(connection);

            if(Boolean.TRUE.equals(imageDAO.existsImageId(imageId,campagnaName))){
                image = imageDAO.getImageDetails(imageId);
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Image's id doesn't exist");
                return;
            }

            String json = new Gson().toJson(image);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        }catch (NumberFormatException number){

            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("ID must be an integer, latitude and longitude must be a double number");
            }catch (IOException ignored) {
            }

        }catch(SQLException e) {
            e.printStackTrace();
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Not possible to recover image's details");
            } catch (IOException ignore){

            }
        } catch (IOException ignore){

        }

//        String path = getServletContext().getContextPath() + "/GetAnnotations";
//        response.sendRedirect(path);

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
