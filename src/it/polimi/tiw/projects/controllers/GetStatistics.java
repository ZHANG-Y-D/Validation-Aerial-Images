package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.dao.CampaignDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetStatistics")
public class GetStatistics extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public GetStatistics() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        try {

            String driver = servletContext.getInitParameter("dbDriver");
            String url = servletContext.getInitParameter("dbUrl");
            String user = servletContext.getInitParameter("dbUser");
            String password = servletContext.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String campagnaName = null;
        CampaignDAO cDAO = null;
        List<Integer> imagesId;
        int totalImage = 0;
        int totalAnnotation = 0;
        int annotationConflics = 0;
        int average = 0;

        try{
//            HttpSession session = request.getSession(false);
//            if (session == null || session.getAttribute("CampaignName") == null) {
//                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                response.getWriter().println("Can't find campaign name");
//                return;
//            }
//            campagnaName = (String) session.getAttribute("CampaignName");
            campagnaName ="Esse";

            cDAO = new CampaignDAO(connection,campagnaName);
            imagesId = cDAO.countImage();
            totalImage = imagesId.size();
            for(int id : imagesId){
                totalAnnotation = totalAnnotation + cDAO.countAnnotationPerImage(id);
                boolean b = cDAO.isAnnotationInConflicts(id);
                if(b){
                    annotationConflics ++;
                }
            }
            average = totalAnnotation / totalImage;

            String path = "statistics.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

            ctx.setVariable("campaignName", campagnaName);
            ctx.setVariable("totalImage", totalImage);
            ctx.setVariable("totalAnnotation", totalAnnotation);
            ctx.setVariable("average", average);
            ctx.setVariable("totalConflicts", annotationConflics);

            templateEngine.process(path, ctx, response.getWriter());

        }catch (SQLException e){

            try {
                response.sendError(500, "Database access failed");
            } catch (IOException ignore){

            }
        } catch (IOException ignore){

        }

    }

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignore) {
        }
    }

}
