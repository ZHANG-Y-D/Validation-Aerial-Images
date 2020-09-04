package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.Image;
import it.polimi.tiw.projects.dao.ImageDAO;

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
import java.util.List;

@WebServlet("/DownloadImage")
public class DownloadImage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public DownloadImage() {
        super();
    }

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        ImageDAO imageDAO = new ImageDAO(connection);
        List<Image> images;
        String campagnaName = request.getParameter("CampaignName");


        try {
            images = imageDAO.findImagesByCampagnaName(campagnaName);
            String json = new Gson().toJson(images);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException | SQLException e) {
            try {
                response.sendError(500, "Database access failed");
            }catch (IOException ignore){

            }

        }

    }


    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignore) {

        }
    }

}
