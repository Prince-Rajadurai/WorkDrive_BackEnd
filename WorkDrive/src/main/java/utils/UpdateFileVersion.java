package utils;

import dao.ResourceManager;

public class UpdateFileVersion {
	
	public static String getUpdatedFileVersion(long fileId) {
		
		int versionCount = 1;
		int brk = 0;
		while(brk == 0) {
			versionCount++;
			brk = checkCurrentVersion(fileId, "#"+versionCount);
		}
		return "#"+versionCount;
	}
	
	public static int checkCurrentVersion(long fileId , String version) {
		
		boolean value = ResourceManager.getExsistingFileVersion(fileId, version);
		
		return value ? 0 : 1;
	}

}
