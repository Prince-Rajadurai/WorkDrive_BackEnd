package servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;

import dao.AccountsManager;
import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ConverByteToString;
import utils.FileOperations;
import utils.GetFileCheckSum;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderUploadServlet
 */
@MultipartConfig
public class FolderUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FolderUploadServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			Cookie[] cookies = request.getCookies();
			String cookieValue = null;
			long userId = 0;

			if (cookies != null) {
			    for (Cookie cookie : cookies) {
			        if ("cookie".equals(cookie.getName())) {
			            cookieValue = cookie.getValue();
			            break;
			        }
			    }
			    userId = AccountsManager.getUserId(cookieValue);
			}
			
			String parentId = request.getParameter("parentId");

			Collection<Part> parts = request.getParts();

			for (Part part : parts) {
				
				String folderId = parentId;
				
				if (part.getName().equals("files")) {
					
					String path = part.getSubmittedFileName();
					String[] nestedPaths = path.split("/");

					for (int i = 0; i < nestedPaths.length; i++) {
					
						if (i == nestedPaths.length - 1) {
					
							String upload = FileOperations.UploadFile(part, folderId, nestedPaths[i]);
							
							MessageDigest md = GetFileCheckSum.getFileCheckSumValue(part);
							String checkSum = ConverByteToString.convertByteToString(md);
							
							String filepath = ResourceManager.getFilePathUsingCheckSum(checkSum);
							
							if(filepath == null || filepath.isEmpty()) {
								String result = FileOperations.UploadFile(part, folderId, nestedPaths[i]);
								filepath = "/"+folderId+"/"+nestedPaths[i];
							}
							
							long fileId = ResourceManager.AddFile(Long.parseLong(folderId), nestedPaths[i],userId); // Add file
							long dfsId = ResourceManager.addDFSFiles(filepath, checkSum, fileId , Long.parseLong(folderId)); // Add file to dfs
							boolean res = ResourceManager.addFileVersion(dfsId);
						
						} else {
							folderId=String.valueOf(ResourceManager.findOrCreateFolder(folderId,nestedPaths[i],userId));
						}
					}

				}
			}
			
			response.getWriter().write(RequestHandler.sendResponse(200, "Folder Uploaded successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}

	}

}
