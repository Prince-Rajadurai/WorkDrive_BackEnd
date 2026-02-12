package utils;

import dao.ResourceManager;

public class UpdateFileVersion {

	public static int getUpdatedFileVersion(long dfsId) {
		
		int updateVersion = ResourceManager.getFileVersion(dfsId);
		updateVersion++;
		
		return updateVersion;
	}

}
