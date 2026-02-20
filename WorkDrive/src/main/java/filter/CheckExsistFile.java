package filter;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import utils.ConverByteToString;
import utils.FileOperations;
import utils.GetFileCheckSum;
import utils.RequestHandler;
import utils.UpdateFileVersion;

import java.io.IOException;
import java.security.MessageDigest;

import dao.ResourceManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class CheckExsistFile
 */
//@WebFilter("/secure/*")
public class CheckExsistFile extends HttpFilter implements Filter {

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public CheckExsistFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String filename = request.getParameter("filename");
		String folderid = request.getParameter("folderId");
		String updateFile = request.getParameter("replaceFile");
		String originalSize = request.getParameter("size");
		String uploadId = request.getParameter("uploadId");

		long original_size = Long.parseLong(originalSize);
		long folderId = Long.parseLong(folderid);
		boolean checkReplace = Boolean.parseBoolean(updateFile);

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Part file = httpRequest.getPart("file");

		long fileId = ResourceManager.findFileIdUsingFilename(folderId, filename);

		MessageDigest md = GetFileCheckSum.getFileCheckSumValue(file);
		String checkSum = ConverByteToString.convertByteToString(md);
		String checkDuplicate = ResourceManager.checkExsistingFile(checkSum, folderId);
		boolean existFileName = ResourceManager.checkExsistingFileName(folderId, filename);
		
		boolean updateFileVersionResult = false;

		if (checkReplace) {

			fileId = ResourceManager.getFileIdUsingCheckSum(checkSum, folderId);

			long dfsId = ResourceManager.findDfsId(fileId);

			long size = ResourceManager.getFileSize(fileId);

			if (checkSum.equals(checkDuplicate)) {

				boolean updateFileSize = ResourceManager.updateFileSize(checkSum, size);
				int updateVersion = UpdateFileVersion.getUpdatedFileVersion(dfsId);
				updateFileVersionResult = ResourceManager.addNewFileVersion(dfsId, updateVersion,
						"/" + folderId + "/" + filename);

			} else {

				fileId = ResourceManager.getFileIdUsingFileName(folderId, filename);
				String filePath = ResourceManager.getFilePath(fileId);
				int pathCount = ResourceManager.checkExistPaths(filePath);
				String deleteResult = "";
				if (pathCount != 2) {
					deleteResult = FileOperations.DeleteFile(filePath);
				}

				FileOperations.UploadFile(file, folderid, filename, uploadId);

				boolean updatePath = ResourceManager.updateDfsPath("/" + folderId + "/" + filename, checkSum, fileId);

				dfsId = ResourceManager.findDfsId(fileId);
				size = ResourceManager.getFileSize(fileId);
				int updateVersion = UpdateFileVersion.getUpdatedFileVersion(dfsId);
				updateFileVersionResult = ResourceManager.addNewFileVersion(dfsId, updateVersion,
						"/" + folderId + "/" + filename);

			}

			if (updateFileVersionResult) {
				response.getWriter().write(RequestHandler.sendResponse(200, "File version updated"));
			} else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File version updated failed"));
			}

		} else if ((checkDuplicate.equals(checkSum)) || existFileName) {
			response.getWriter().write(RequestHandler.sendResponse(400, "File already exists"));
		} else {
			chain.doFilter(request, response);
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
