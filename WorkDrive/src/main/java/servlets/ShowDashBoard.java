package servlets;

import java.io.IOException;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class ShowDashBoard
 */
//@WebServlet("/ShowDashBoard")
public class ShowDashBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ShowDashBoard() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		long userId  = GetUserId.getUserId(request);
		
		long storage = ResourceManager.getOriginalSize(userId);
		
		long compress = ResourceManager.getCompressedSize(userId);
		
		long dFiles = ResourceManager.getDeduplicateFiles(userId);
		
		long files = ResourceManager.getTotalFiles(userId);
		
		long deduplicateSize = ResourceManager.getDuplicateFilesSize(userId);
		
		float storagePercentage = 0.0f , duplicateSizePercentage = 0.0f , duplicateFilePercentage = 0.0f;
		
		if(storage-compress>deduplicateSize) {
			 storagePercentage = (float) ((storage-compress)-deduplicateSize)/storage * 100;
		}
		else {
			storagePercentage = (float) (deduplicateSize-(storage-compress))/storage * 100;
		}
		
		
		
		duplicateSizePercentage = (float) deduplicateSize/storage*100;
		
		duplicateFilePercentage = (float) dFiles/files*100;
		
		
		response.getWriter().write(RequestHandler.sendResponse(200,FileOperations.converFileSizeToString(storage),FileOperations.converFileSizeToString(storage-compress) , files , files-dFiles, storagePercentage , duplicateFilePercentage , duplicateSizePercentage));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
