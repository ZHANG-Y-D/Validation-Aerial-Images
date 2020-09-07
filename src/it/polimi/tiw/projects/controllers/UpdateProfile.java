package it.polimi.tiw.projects.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.UserDAO;
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
import java.sql.SQLException;


@WebServlet("/UpdateProfile")
@MultipartConfig
public class UpdateProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public UpdateProfile() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        UserDAO userDao = new UserDAO(connection);
        // obtain and escape params
        String pwd = null;
        String confirmed = null;
        String email = null;
        String lavoratoreLevel = null;
        InputStream lavoratoreFoto = null;

        try {
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            confirmed = StringEscapeUtils.escapeJava(request.getParameter("confirmed"));
            email = StringEscapeUtils.escapeJava(request.getParameter("email"));

            if (pwd == null || confirmed == null || email == null ||
                    pwd.isEmpty() || confirmed.isEmpty() || email.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Credentials must be not null");
                return;
            }

            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find user");
                return;
            }
            User user = (User) session.getAttribute("user");

            if (user.getRole().equals("Lavoratore")){
                lavoratoreLevel = StringEscapeUtils.escapeJava(request.getParameter("lavoratoreLevel"));
                lavoratoreFoto = request.getPart("image").getInputStream();
                if (lavoratoreLevel == null || lavoratoreLevel.isEmpty() || lavoratoreFoto == null){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Credentials must be not null");
                    return;
                }
            }

            if (!pwd.equals(confirmed)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println("Password and confirm password must be the same.");
                return;
            }

            User newUserInfo = userDao.updateInformation(user.getUsername(), pwd, email, lavoratoreLevel,lavoratoreFoto);

            //Set new Session
            request.getSession().setAttribute("user", newUserInfo);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String userJson = new Gson().toJson(newUserInfo);
            response.getWriter().write(userJson);

        } catch (SQLException sqlException) {
            try {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println(sqlException.getMessage());
            } catch (IOException ignore){

            }

        }  catch (ServletException e) {
            e.printStackTrace();
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
