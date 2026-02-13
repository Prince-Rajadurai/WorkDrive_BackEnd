package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.CheckDuplicateFile;
import utils.ConverByteToString;
import utils.FileOperations;
import utils.FileRename;
import utils.GetFileCheckSum;
import utils.GetUserId;
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
		long folderId = Long.parseLong(folderid);
		
		String filename = request.getParameter("filename");
		String folderid = request.getParameter("folderId");
		long folderId = Long.parseLong(folderid);
		
		String newFileName = CheckDuplicateFile.getFileName(folderId, filename);
		filename = newFileName;
		Part file1 = request.getPart("file");
		try {
			MessageDigest md = GetFileCheckSum.getFileCheckSumValue(file1);
			String checkSum = ConverByteToString.convertByteToString(md);
			
			long userId = GetUserId.getUserId(request);
			
			String filepath = ResourceManager.getFilePathUsingCheckSum(checkSum);
			
			if(filepath == null || filepath.isEmpty()) {
				String result = FileOperations.UploadFile(file1 , folderid , filename);
				filepath = "/"+folderid+"/"+filename;
			}
			
			long fileId = ResourceManager.AddFile( folderId, filename,  userId); // Add file
			long dfsId = ResourceManager.addDFSFiles(filepath, checkSum, fileId , folderId , FileOperations.getSize(filepath)); // Add file to dfs
			boolean res = ResourceManager.addFileVersion(dfsId); // Add file version
			
			if(res) {
				response.getWriter().write(RequestHandler.sendResponse(200, "File uploaded successfully"));
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File uploaded failed"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
