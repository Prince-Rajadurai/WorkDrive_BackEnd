package utils;

public class FileRename {
	
	public static String getFileExtension(String fileName) {
		
		if(fileName.contains(".")) {
			String[] separateFileName = fileName.split("\\.");
			
			return separateFileName[1];
		}
		else {
			return "";
		}
	}
	
	public static String getFileName(String fileName) {
		
		if(fileName.charAt(0)=='.') {
			fileName = fileName.replaceFirst(".", "");
			if(fileName.indexOf(".")==-1) {
				return fileName;
			}
		}
		else if(!fileName.contains(".")) {
			return fileName;
		}
		
		String[] separateFileName = fileName.split("\\.");
		
		return separateFileName[0];
	}

}
