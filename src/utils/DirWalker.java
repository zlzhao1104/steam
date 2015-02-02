package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirWalker {
    
    public static List<String> listFiles(String dir, String ext) {
	List<String> list = new ArrayList<>();
	
	File folder = new File(dir);
	File[] files = folder.listFiles();
	
	for (int i = 0; i < files.length; i++) {
	    if (files[i].isFile()) {
		String fileName = files[i].getName();
		int id = fileName.lastIndexOf('.');
		if (id > 0) {
		    if (fileName.substring(id + 1).equals(ext)) {
			list.add(files[i].getAbsolutePath());
		    };
		}
	    }
	}
	
	return list;
    }

}
