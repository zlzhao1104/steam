/*
Copyright (C) <2015>  <Ziliang Zhao and Shih-Lung Shaw>

STEAM is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

STEAM is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with STEAM.  If not, see <http://www.gnu.org/licenses/>
*/

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
