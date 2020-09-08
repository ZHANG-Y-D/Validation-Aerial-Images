package it.polimi.tiw.projects.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.projects.beans.User;


public class ManagerChecker implements Filter {


    public ManagerChecker() {
        // TODO Auto-generated constructor stub
    }


    public void destroy() {
        // TODO Auto-generated method stub
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.print("Manager filter executing ..\n");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String loginpath = req.getServletContext().getContextPath() + "/loginPage.html";
        // check if the client is an admin
        HttpSession session = req.getSession(false);
        User user = null;

        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(loginpath);
            return;
        }

        user = (User) session.getAttribute("user");
        if (!user.getRole().equals("Manager")) {
            res.sendRedirect(loginpath);
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}