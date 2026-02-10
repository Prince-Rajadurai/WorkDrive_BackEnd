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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        try {
        	Long parentId;
        	long userId = 0;
        	Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("cookie".equals(cookie.getName())) {
                        userId = AccountsManager.getUserId(cookie.getValue());
                        break;
                    }
                }
            }
            String parentIdParam = request.getParameter("parentId");
            if (parentIdParam == null || parentIdParam.equals("null")) {
            	parentId = ResourceManager.getMyFolderId(userId);
            } else {
                parentId = Long.parseLong(parentIdParam);
            }
            String cursorParam = request.getParameter("cursor");
            String limitParam = request.getParameter("limit");
            long cursor = cursorParam != null ? Long.parseLong(cursorParam) : 0L;
            int limit = limitParam != null ? Integer.parseInt(limitParam) : 20;
            ArrayList<JSONObject> folders = ResourceManager.getResource(parentId, userId, cursor, limit);
            ArrayList<JSONObject> files = ResourceManager.getAllFiles(parentId , userId, cursor, limit);
            ArrayList<JSONObject> resources = new ArrayList<>();
            for (JSONObject folder : folders) {
                folder.put("type", "FOLDER");
                folder.put("id", folder.getLong("resourceId"));
                folder.put("cursorId", folder.getLong("resourceId"));
                resources.add(folder);
            }
            for (JSONObject file : files) {
                file.put("type", "FILE");
                file.put("id", file.getLong("id"));
                file.put("cursorId", file.getLong("id"));
                resources.add(file);
            }
            long nextCursor = 0;
            if (!resources.isEmpty()) {
            	JSONObject lastResource = resources.get(resources.size() - 1);
            	nextCursor = lastResource.getLong("CreatedTime");
            }
            resources.sort((a, b) ->
            	Long.compare(a.getLong("cursorId"), b.getLong("cursorId"))
            );
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(RequestHandler.sendResponse(200, "Resources rendered successfully", resources , String.valueOf(parentId), nextCursor));
        } catch (Exception e) {
        	e.printStackTrace();
            response.getWriter().write(RequestHandler.sendResponse(500, "Failed to render resource"));
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}