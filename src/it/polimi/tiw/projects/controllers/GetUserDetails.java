package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;

@WebServlet("/GetUserDetails")
public class GetUserDetails extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public GetUserDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }
}
