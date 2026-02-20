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
		
		long files = ResourceManager.getTotalFiles(userId , "FILE");
		
		long deduplicateSize = ResourceManager.getDuplicateFilesSize(userId);
		
		long folderCount = ResourceManager.getTotalFiles(userId, "FOLDER");
		
		float storagePercentage = 0.0f;
		float duplicateSizePercentage = 0.0f;
		float duplicateFilePercentage = 0.0f;

		if (storage > 0) {

		    storagePercentage = ((float)((storage - compress)+deduplicateSize) / storage) * 100;

		    duplicateSizePercentage = ((float) deduplicateSize / storage) * 100;

		    if (files > 0) {
		        duplicateFilePercentage = ((float) dFiles / (float) files) * 100;
		    }
		}
		
		long savedSize = 0;
		
		if(deduplicateSize+compress>storage) {
			savedSize = (deduplicateSize+compress)-storage;
		}
		else {
			savedSize = storage-(deduplicateSize+compress);
		}

		
		response.getWriter().write(RequestHandler.sendResponse(200,FileOperations.converFileSizeToString(storage),FileOperations.converFileSizeToString(deduplicateSize),FileOperations.converFileSizeToString(storage-compress) ,FileOperations.converFileSizeToString(savedSize), files , files-dFiles, storagePercentage , duplicateFilePercentage , duplicateSizePercentage , storage , compress , folderCount));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
