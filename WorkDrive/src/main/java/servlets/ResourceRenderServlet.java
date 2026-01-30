package servlets;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import utils.RequestHandler;
import dao.AccountsManager;
import dao.ResourceManager;

/**
* Servlet implementation class ShowFiles
*/
public class ResourceRenderServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
     
   /**
    * @see HttpServlet#HttpServlet()
    */
   public ResourceRenderServlet() {
       super();
       // TODO Auto-generated constructor stub
   }

   /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // TODO Auto-generated method stub
       response.getWriter().append("Served at: ").append(request.getContextPath());
   }

   /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // TODO Auto-generated method stub
       try {
           JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
           Long parentId = null;
           long userId = 0;
           Cookie[] cookies = request.getCookies();
           String cookieValue = null;
           if (cookies != null) {
               for (Cookie cookie : cookies) {
                   if ("cookie".equals(cookie.getName())) {
                       cookieValue = cookie.getValue();
                       break;
                   }
               }
               userId = AccountsManager.getUserId(cookieValue);
           }
           if (!requestObject.isNull("parentId")) {
               parentId = requestObject.getLong("parentId");
           } else {
               parentId = ResourceManager.getMyFolderId(userId);
           }
           ArrayList<JSONObject> folders = ResourceManager.getResource(parentId, userId);
           ArrayList<JSONObject> files = ResourceManager.getAllFiles(parentId);
           ArrayList<JSONObject> resources = new ArrayList<>();
           resources.addAll(folders);
           resources.addAll(files);
           response.setContentType("application/json");
           response.setCharacterEncoding("UTF-8");
           response.getWriter().write(RequestHandler.sendResponse(200, "Resources rendered successfully", resources));
       } catch (Exception e) {
           e.printStackTrace();
           response.getWriter().write(RequestHandler.sendResponse(500, "Failed to render resource"));
       }
   }

}