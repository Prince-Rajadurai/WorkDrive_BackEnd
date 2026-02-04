package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.FileOperations;
import utils.RequestHandler;

/**
 * Servlet implementation class UpdateFileServlet
 */
@MultipartConfig
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UploadFileServlet() {
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
		
		String filename = request.getParameter("filename");
		String folderid = request.getParameter("folderId");
		String chunkStr = request.getParameter("chunkIndex");
		int chunk = 0;
		if (chunkStr != null && !chunkStr.isEmpty()) {
		        chunk = Integer.parseInt(chunkStr);
		}
		long folderId = Long.parseLong(folderid);
		boolean chk = Boolean.parseBoolean(request.getParameter("addDb"));
		if(chk) {
			boolean res = ResourceManager.AddFile( folderId, filename, FileOperations.getFileSize(folderid+"/"+filename));
			if(res) {
				response.getWriter().write(RequestHandler.sendResponse(200, "File uploaded successfully"));
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File uploaded failed"));
			}
		}
		else {
			Part file1 = request.getPart("file");
			try {
				String result = FileOperations.UploadFile(chunk , file1 , folderid , filename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
