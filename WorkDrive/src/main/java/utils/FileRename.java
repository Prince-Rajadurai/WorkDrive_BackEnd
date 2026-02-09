package utils;

public class FileRename {
	
	public static String getFileExtension(String fileName) {
		
		String[] separateFileName = fileName.split("\\.");
		
		return separateFileName[1];
		
	}
	
	public static String getFileName(String fileName) {
		
		if(fileName.charAt(0)=='.') {
			fileName = fileName.replaceFirst(".", "");
			if(fileName.indexOf(".")==-1) {
				return fileName;
			}
		}
		
		String[] separateFileName = fileName.split("\\.");
		
		return separateFileName[0];
	}

}
