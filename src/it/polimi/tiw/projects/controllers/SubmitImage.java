package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.dao.ImageDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/SubmitImage")
@MultipartConfig
public class SubmitImage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public SubmitImage() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
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
            //Check status
            latitude = Double.parseDouble(request.getParameter("latitude"));
            longitude = Double.parseDouble(request.getParameter("longitude"));
            comune = StringEscapeUtils.escapeJava(request.getParameter("comune"));
            regione = StringEscapeUtils.escapeJava(request.getParameter("regione"));
            provenienza = StringEscapeUtils.escapeJava(request.getParameter("provenienza"));
            date = java.sql.Date.valueOf(request.getParameter("date"));
            risoluzione = StringEscapeUtils.escapeJava(request.getParameter("risoluzione"));
            imageStream = request.getPart("image").getInputStream();


            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CampaignName") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }
            campagnaName = (String) session.getAttribute("CampaignName");

            if (latitude == 90.1 || longitude == 180.1 || comune == null ||
                    regione == null || provenienza == null || risoluzione == null ||
                    date == null || campagnaName == null || imageStream == null ||
                    comune.isEmpty() || regione.isEmpty() || provenienza.isEmpty() ||
                    risoluzione.isEmpty()) {

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

    @Override
    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
