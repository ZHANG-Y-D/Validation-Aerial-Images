package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.WorkerDAO;
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
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/writeAnnotation")
@MultipartConfig
public class WriteAnnotation extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public WriteAnnotation() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int imageId;
        Date date = new Date(java.util.Calendar.getInstance().getTime().getTime());
        String validita = null;
        int v;
        String fiducia = null;
        String note = null;
        String campagnaName = null;
        User user = null;

        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("user") == null ||
                    session.getAttribute("CampaignName") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find user or campaign");
                return;
            }
            user = (User) session.getAttribute("user");
            campagnaName = (String) session.getAttribute("CampaignName");

            if(user == null  || campagnaName == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Invalid access");
                return;
            }

            WorkerDAO workerDAO = new WorkerDAO(connection, user.getUsername());

            imageId = Integer.parseInt(request.getParameter("imageId"));
            validita = StringEscapeUtils.escapeJava(request.getParameter("validita"));
            if(validita.equals("vero")){
                v = 1;
            }else v = 0;
            fiducia =  StringEscapeUtils.escapeJava(request.getParameter("fiducia"));
            if(request.getParameter("note") !=null){
                note = StringEscapeUtils.escapeJava(request.getParameter("note"));
            }

            if(workerDAO.isStarted(campagnaName)){
                String resultString = workerDAO.insertAnnotation(imageId,date,v,fiducia,note);
                if (resultString.contains("OK")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                }else {
                    response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    response.getWriter().println(resultString);
                }
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Campaign is closed");
            }
        } catch (NumberFormatException numberFormatException){
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("imageId is a integer");
            } catch (IOException ignore){

            }

        }
        catch (SQLException e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error, retry later. "+e.getMessage());
            } catch (IOException ignore){

            }

        } catch (IOException ignore){

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
