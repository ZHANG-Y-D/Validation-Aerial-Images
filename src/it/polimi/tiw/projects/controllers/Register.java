package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/Register")
@MultipartConfig
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public Register() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {


		UserDAO userDao = new UserDAO(connection);
		String resultString = null;
		// obtain and escape params
		String usrn = null;
		String pwd = null;
		String confirmed = null;
		String email = null;
		String role = null;
		String lavoratoreLevel = null;
		InputStream lavoratoreFoto = null;

		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			confirmed = StringEscapeUtils.escapeJava(request.getParameter("confirmed"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			role = StringEscapeUtils.escapeJava(request.getParameter("role"));


			if (usrn == null || pwd == null || confirmed == null || email == null || role == null ||
					usrn.isEmpty() || pwd.isEmpty() || confirmed.isEmpty() || email.isEmpty() || role.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Credentials must be not null");
				return;
			}

			if (role.equals("Lavoratore")){
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

			resultString = userDao.insertForRegister(usrn, pwd, email, role, lavoratoreLevel,lavoratoreFoto);
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
			} catch (IOException ignore){

			}
		} catch (IOException ignore){

		} catch (ServletException e) {
			e.printStackTrace();
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
