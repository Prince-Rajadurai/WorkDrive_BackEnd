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
		
				
		String path = requestObject.getString("path");
		String filename = requestObject.getString("filename");
		String folderid = requestObject.getString("folderId");
		String localFile = requestObject.getString("localfile");
		long folderId = Long.parseLong(folderid); 
		String result = FileOperations.UploadFile( folderid , path , localFile , filename);//-> change
		
		if(result.equals("File uploaded sucessfully")) {
			
			boolean res = ResourceManager.AddFile(folderId, path+"/"+filename, filename);
			if(res) {
				responseObject.put("StatusCode", 200);
				responseObject.put("message", "File uploaded sucessfully");
			}
			
		}
		else {
				responseObject.put("StatusCode", 400);
				responseObject.put("message", "File upload failed");
		}
		
		response.getWriter().write(responseObject.toString());
	}

}
