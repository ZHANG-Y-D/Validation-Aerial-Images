package it.polimi.tiw.projects.filters;

import it.polimi.tiw.projects.beans.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WorkerChecker  implements Filter {

    public WorkerChecker(){
        super();
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.print("Worker filter executing ..\n");
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
        if (!user.getRole().equals("Lavoratore")) {
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
