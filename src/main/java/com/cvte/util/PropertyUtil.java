package com.cvte.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

/** 
* @author: jan 
* @date: 2018年5月9日 下午2:17:20 
*/
public class PropertyUtil {
	private static final String clientPropPath = "/user.properties";
	
	public static String Account;
	
	public static String Password;
	
	public static String User;
	
	public static String Rem;

	public boolean loadProperty() {
		
		try {
			Properties prop = new Properties();// 属性集合对象
			String rootPath = System.getProperty("user.dir").replace("\\", "/");
			FileInputStream fis = new FileInputStream(rootPath + clientPropPath);
			
			InputStreamReader isr = null;
			isr = new InputStreamReader(fis, "UTF-8");
			prop.load(isr);// 将属性文件流装载到Properties对象中   
			fis.close();// 关闭流 
			
			Account = prop.getProperty("account", "");
			Password = prop.getProperty("password", "");
			Rem = prop.getProperty("rem", "");
			User = prop.getProperty("user", "");
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		PropertyUtil util = new PropertyUtil();
		util.loadProperty();
		System.out.println(PropertyUtil.Account);
		System.out.println(PropertyUtil.Password);
	}

	public void writeProp(String user, String rem) {
		try{
			String rootPath = System.getProperty("user.dir").replace("\\", "/");
			File f5=new File(rootPath + clientPropPath);
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f5),"UTF-8"); 
			//FileWriter fw=new FileWriter(f5);
	        BufferedWriter bw=new BufferedWriter(osw);
	        bw.write("account=" + PropertyUtil.Account);
	        bw.newLine();
	        bw.write("password=" + PropertyUtil.Password);
	        bw.newLine();
	        bw.write("rem=" + rem);
	        bw.newLine();
	        bw.write("user=" + user);
			bw.close();
			osw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
