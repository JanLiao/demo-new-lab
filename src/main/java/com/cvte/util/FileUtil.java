package com.cvte.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/** 
* @author: jan 
* @date: 2018年5月10日 上午3:31:18 
*/
public class FileUtil {

	public static boolean copyFile(String dir, String imgPath, String name) {
		File srcFile = new File(dir + "/" + name);
		File destFile = new File(imgPath + "/" + name);
		try {
			FileUtils.copyFile(srcFile, destFile);
			//System.out.println("复制成功");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
