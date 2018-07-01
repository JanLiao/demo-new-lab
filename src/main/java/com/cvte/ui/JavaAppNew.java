package com.cvte.ui;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cvte.cons.Constant;
import com.cvte.util.PropertyUtil;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * @author: jan
 * @date: 2018年5月9日 下午2:14:58
 */
//JavaScript interface object
public class JavaAppNew {
	
	public static WebEngine webEngineNew;

	// login登录    有时间加一个人工session
	public void login(String account, String password) {
		System.out.println("登录 = " + account + " , " + password);
		String md5 = "";
		try {
			md5 = getMD5Str(password.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PropertyUtil prop = new PropertyUtil();
		prop.loadProperty();
		if(account.trim().equals(PropertyUtil.Account) && md5.equals(PropertyUtil.Password)) {
			Browser.webEngine.executeScript("loginJava('0')");
		}else {
			//Browser.webEngine.executeScript("loginJava('1')");
			Browser.webEngine.executeScript("loginJava('0')");
		}
	}

	// 关闭界面
	public void exit() {
		Platform.exit();
	}
	
	public static String getMD5Str(String str) throws Exception {  
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            return new BigInteger(1, md.digest()).toString(16);  
        } catch (Exception e) {  
            throw new Exception("MD5加密出现错误，"+e.toString());  
        }  
    }  
	
	public void openDirectory() {
		Stage fileStage = null;
	    DirectoryChooser folderChooser = new DirectoryChooser();
	    folderChooser.setTitle("Choose Folder");
	    File selectedFile = folderChooser.showDialog(fileStage);
	    System.out.println("dir = " + selectedFile);
	    String dir = selectedFile.getAbsolutePath().replace('\\', '/');
	    int code = 0;
	    if(Constant.DirName.size() == 0) {
	    	Constant.DirName.put(dir, 1);
	    	System.out.println("dirr = " + dir);
		    File[] files = new File(dir).listFiles();
		    System.out.println("name = " + files[0].getName());
		    code = checkDir(files);
	    }else {
	    	for(String s : Constant.DirName.keySet()) {  //判断文件夹是否已选择
	    		if(s.equals(dir)) {
	    			//Constant.Flag = 1;
	    			if(Constant.DirName.get(dir) == 0) {
	    				Constant.DirName.put(dir, 1);
	    		    	System.out.println("dirr = " + dir);
	    			    File[] files = new File(dir).listFiles();
	    			    System.out.println("name = " + files[0].getName());
	    			    code = checkDir(files);
	    			    //Constant.Flag = 0;
	    			}else {
	    				code = -1;
	    			}
	    			break;
	    		}
	    	}
//			if (Constant.Flag == 0) {
//				Constant.DirName.put(dir, 1);
//				System.out.println("dirr = " + dir);
//				File[] files = new File(dir).listFiles();
//				System.out.println("name = " + files[0].getName());
//				code = checkDir(files);
//			}
	    }
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
	    if(code > 0) {
	    	Constant.DirNum += 1;
	    	Constant.DirList.add(dir);
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory('" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }else {
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory('" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }
	}

	public int checkDir(File[] files) {
		int num = 0;
		for(File f : files) {
			if(!f.isDirectory()) {
				if(checkImg(f)) {
					num++;
				}
			}
		}
		return num;
	}

	public boolean checkImg(File f) {
		String[] str = f.getName().split("[.]");
		if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
				|| "jpeg".equals(str[1].toLowerCase())) {
			return true;
		}else {
			return false;
		}
	}
	
	public void delDirectory(String offset) {
		System.out.println("dirNum = " + offset + "=" + Constant.DirList.get(Integer.parseInt(offset) - 1));
		Constant.DirName.put(Constant.DirList.get(Integer.parseInt(offset) - 1), 0);
	}
}