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
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo da aggiungere dopo

        //                Campaign campaign = new Campaign();
//        		HttpSession s = request.getSession();
//         		campaign = (Campaign) s.getAttribute("campaign");
        String campagnaName = "Esse";

        //todo controllare se il nome del parametro e' giusto
        int imageId = 1;
                // int imageId = Integer.parseInt(request.getParameter("imageId"));
        Image image;
        ImageDAO imageDAO = new ImageDAO(connection);



        try {
            if(imageDAO.existsImageId(imageId,campagnaName)){
                image = imageDAO.getImageDetails(imageId);
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Image's id doesn't exist");
                return;

            }

        }catch(SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Not possible to recover image's details");
            return;
        }
        String json = new Gson().toJson(image);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);


//        String path = getServletContext().getContextPath() + "/GetAnnotations";
//        response.sendRedirect(path);

    }

    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
