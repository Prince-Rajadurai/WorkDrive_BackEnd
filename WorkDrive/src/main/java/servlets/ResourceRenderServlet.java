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
import utils.Resources;
import utils.File;
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
            long parentId = (parentIdParam == null || parentIdParam.equals("null")) ? ResourceManager.getMyFolderId(userId) : Long.parseLong(parentIdParam);
            String folderCursorParam = request.getParameter("folderCursor");
            long folderCursor = (folderCursorParam == null) ? 0 : Long.parseLong(folderCursorParam);
            
            String fileCursorParam = request.getParameter("fileCursor");
            long fileCursor = (fileCursorParam == null) ? 0 : Long.parseLong(fileCursorParam);

            String limitParam = request.getParameter("limit");
            int limit = (limitParam == null || limitParam.equals("0")) ? 18 : Integer.parseInt(limitParam);
            int fetchLimit = limit + 1;
            
            String sortBy = request.getParameter("sortBy");
            if (sortBy == null || sortBy.equalsIgnoreCase("null")) sortBy = "name";
            String sortOrder = request.getParameter("sortOrder");
            if (sortOrder == null || sortOrder.equalsIgnoreCase("null")) sortOrder = "asc";
            
            ArrayList<JSONObject> resources = new ArrayList<>();
            long lastFolderCursor = folderCursor;
            long lastFileCursor = fileCursor;
            
            boolean hasMoreFolders = false;
            boolean hasMoreFiles = false;
            
            boolean fetchingFolders = folderCursor != -1;
            
//            if (!sortBy.equalsIgnoreCase("resourceId")) {
//            	folderCursor = 0;
//            	fileCursor = 0;
//            }
            
            if (fetchingFolders) {
            	ArrayList<JSONObject> Folders = ResourceManager.getResources("FOLDER", parentId, userId, folderCursor, fetchLimit, sortBy, sortOrder);
            	for (JSONObject folder : Folders) {
            		JSONObject obj = Resources.addingJson(folder);
            		resources.add(obj);            		
                }
            	if (Folders.size() > limit) {
            		hasMoreFolders = true;
            		Folders.remove(Folders.size() - 1);
            	}
            	if(!Folders.isEmpty()) {
                	lastFolderCursor = Folders.get(Folders.size() - 1).getLong("resourceId");
                }
            }
            
            int remainingLimit = limit - resources.size();
            
            if (remainingLimit > 0) {
            	ArrayList<JSONObject> Files = ResourceManager.getResources("FILE", parentId, userId, fileCursor, remainingLimit + 1, sortBy, sortOrder);
            	for (JSONObject file : Files) {
                    JSONObject obj = File.addingJson(file);
                    resources.add(obj);
                }
            	if (Files.size() > remainingLimit) {
            		hasMoreFiles = true;
            		Files.remove(Files.size() - 1);
            	}
                if(!Files.isEmpty()) {
                	lastFileCursor = Files.get(Files.size() - 1).getLong("id");
                }
            }
            boolean hasMore = hasMoreFolders || hasMoreFiles;
            JSONObject cursors = new JSONObject();
            cursors.put("folderCursor", hasMoreFolders ? lastFolderCursor : -1);
            cursors.put("fileCursor", hasMoreFiles ? lastFileCursor : -1);
            cursors.put("hasMore", hasMore);
            response.getWriter().write(
                RequestHandler.sendResponse(200, "Resources rendered successfully", resources, String.valueOf(parentId), cursors)
            );

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