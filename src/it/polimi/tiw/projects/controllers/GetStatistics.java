package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.dao.CampaignDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/GetStatistics")
public class GetStatistics extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public GetStatistics(){
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> statistics = new ArrayList<Integer>();
        List<Integer> imagesId;
        int totalImage;
        int totalAnnotation = 0;
        int annotationConflics = 0;
        int average ;

        try{
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CampaignName") == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().println("Can't find campaign name");
                return;
            }
            String campagnaName = (String) session.getAttribute("CampaignName");
           // String campaign = "";
            CampaignDAO cDAO = new CampaignDAO(connection,campagnaName);

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
           statistics.add(totalImage);
           statistics.add(totalAnnotation);
           statistics.add(average);
           statistics.add(annotationConflics);

            String json = new Gson().toJson(statistics);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        }catch (SQLException e){
            try {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Not possible to count");
            }catch (IOException ignore){

            }
        } catch (IOException ignore){

        }



    }
}
