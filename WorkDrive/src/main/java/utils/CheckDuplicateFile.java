package utils;

import dao.ResourceManager;

public class CheckDuplicateFile {

	public static String getFileName(long folderId , String fileName) {
		
		String filename = FileRename.getFileName(fileName);
		String extension = FileRename.getFileExtension(fileName);
		int brk = 0;
		int fileNumber = 0;

		while (brk == 0) {

		    if (fileNumber == 0) {
		        brk = checkDuplicateFileName(folderId, fileName);
		        if (brk == 1) {
		            return fileName;
		        }
		    } 
		    else {

		        String baseFileName = filename;

		        String newFileName = baseFileName + " (" + fileNumber + ")." + extension;

		        brk = checkDuplicateFileName(folderId, newFileName);
		    }

		    if (brk == 0) {
		        fileNumber++;
		    }

		}

		return extension!=""?filename + " (" + fileNumber + ")." + extension:filename + " (" + fileNumber + ")" + extension;
	}
	
	public static int checkDuplicateFileName(long folderId , String fileName) {
		boolean res = ResourceManager.checkExsistingFileName(folderId, fileName);
		
		return res ? 0 : 1;
	}
	
}
