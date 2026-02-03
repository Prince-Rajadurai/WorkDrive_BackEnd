package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.RequestHandler;

/**
 * Servlet implementation class UpdateFileServlet
 */
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
		
		JSONObject requestObject = (JSONObject) request.getAttribute("fileDetails");
		JSONObject responseObject = new JSONObject();
		
		String filename = requestObject.getString("filename");
		String folderid = requestObject.getString("folderId");
		String localFileData = requestObject.getString("data");
		System.out.print(localFileData);
		long folderId = Long.parseLong(folderid); 
		String result = FileOperations.UploadFile( folderid , localFileData , filename);
		
		if(result.equals("File uploaded sucessfully")) {
			
			String size = FileOperations.getFileSize(folderid+"/"+filename);
			boolean res = ResourceManager.AddFile(folderId, filename , size);
			if(res) {
				response.getWriter().write(RequestHandler.sendResponse(200, "file uploaded"));
			}
			
		}
		else {
			response.getWriter().write(RequestHandler.sendResponse(400, "file upload failed"));	
		}
	}

}
