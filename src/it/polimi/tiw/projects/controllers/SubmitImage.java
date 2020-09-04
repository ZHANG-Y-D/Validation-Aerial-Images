package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.dao.ImageDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/SubmitImage")
@MultipartConfig
public class SubmitImage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public SubmitImage() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        // obtain and escape params
        double latitude = 90.1;
        double longitude = 180.1;
        String comune = null;
        String regione = null;
        String provenienza = null;
        Date date = null;
        String risoluzione = null;
        InputStream imageStream = null;
        String campagnaName = null;

        try {

            latitude = Double.parseDouble(request.getParameter("latitude"));
            longitude = Double.parseDouble(request.getParameter("longitude"));
            comune = request.getParameter("comune");
            regione = request.getParameter("regione");
            provenienza = request.getParameter("provenienza");
            date = java.sql.Date.valueOf(request.getParameter("date"));
            risoluzione = request.getParameter("risoluzione");
            imageStream = request.getPart("image").getInputStream();

            campagnaName = "Esse"; //TODO

            if (latitude == 90.1 || longitude == 180.1 || comune == null ||
                    regione == null || provenienza == null || risoluzione == null ||
                    date == null || campagnaName == null || imageStream == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Information fields must be not null");
                return;
            }
        }catch (NumberFormatException number){
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("ID must be an integer, latitude and longitude must be a double number");
            }catch (IOException ignored){

            }
        }catch (IOException | ServletException imageException){

            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Image Error");
            }catch (IOException ignored){

            }
        }

        // insert into database
        ImageDAO imageDAO = new ImageDAO(connection);
        String resultString;
        try {
            resultString = imageDAO.insertImage(latitude, longitude, comune,
                                                regione, provenienza, date,
                                                risoluzione, campagnaName, imageStream);
            if (resultString.contains("OK")) {
                response.setStatus(HttpServletResponse.SC_OK);
            }else {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println(resultString);
            }
        } catch (SQLException e) {

            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error, retry later. "+e.getMessage());
            } catch (IOException ignored){

            }

        } catch (IOException ignored){

        }
    }

    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
