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
            
            ArrayList<JSONObject> resources = new ArrayList<>();
            long lastFolderCursor = folderCursor;
            long lastFileCursor = fileCursor;

//            ArrayList<JSONObject> folders = ResourceManager.getResource(parentId, userId, folderCursor, fetchLimit);
            ArrayList<JSONObject> folders = new ArrayList<>();
            if (folderCursor != -1) {
            	folders = ResourceManager.getResource(parentId, userId, folderCursor, fetchLimit);
            }
            
            boolean hasMoreFolders = false;
            if (folders.size() > limit) {
            	hasMoreFolders = true;
            	folders.remove(folders.size() - 1);
            }

            int remainingLimit = limit - folders.size();

            ArrayList<JSONObject> files = new ArrayList<>();
            boolean hasMoreFiles = false;
            if (remainingLimit > 0) {
            	int fileLimit = remainingLimit > 0 ? remainingLimit : limit;
                files = ResourceManager.getAllFiles(parentId, userId, fileCursor, fileLimit + 1);
                if (files.size() > fileLimit) {
                	hasMoreFiles = true;
                	files.remove(files.size() - 1);
                }
            }

            for (JSONObject folder : folders) {
                JSONObject obj = new JSONObject();
                obj.put("id", String.valueOf(folder.getLong("resourceId")));
//                obj.put("id", folder.getString("resourceId"));
                obj.put("name", folder.getString("resourceName"));
                obj.put("type", "FOLDER");
                obj.put("createdTime", folder.getString("createdTime"));
                obj.put("modifiedTime", folder.getString("modifiedTime"));
                obj.put("size", folder.getString("size"));
                obj.put("files", folder.getInt("files"));
                obj.put("folders", folder.getInt("folders"));
                resources.add(obj);
            }
            
            for (JSONObject file : files) {
                JSONObject obj = new JSONObject();
                obj.put("id", String.valueOf(file.getLong("id")));
                obj.put("name", file.getString("filename"));
                obj.put("type", "FILE");
                obj.put("createdTime", file.getString("createTime"));
                obj.put("modifiedTime", file.getString("modifiedTime"));
                obj.put("size", file.getString("size"));
                resources.add(obj);
            }
            
            if(!folders.isEmpty()) {
            	lastFolderCursor = folders.get(folders.size() - 1).getLong("resourceId");
            }
            
            if(!files.isEmpty()) {
            	lastFileCursor = files.get(files.size() - 1).getLong("id");
            }
            JSONObject cursors = new JSONObject();
            cursors.put("folderCursor", hasMoreFolders ? lastFolderCursor : -1);
            cursors.put("fileCursor", hasMoreFiles ? lastFileCursor : -1);
            boolean hasMore = hasMoreFolders || (folderCursor == -1 && hasMoreFiles);
            cursors.put("hasMore", hasMore);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
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