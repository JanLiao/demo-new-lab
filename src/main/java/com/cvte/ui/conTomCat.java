package com.cvte.ui;

/** 
* @author: jan 
* @date: 2018年5月14日 下午3:12:35 
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

//java程序控制tomcat启动  
//如不能运行，请正确配置tomcat环境  

public class conTomCat {
	public void close() throws IOException {
		Process process = Runtime.getRuntime().exec("cmd /c  C:\\Users\\CVTE\\Desktop\\标注demo\\tomcat\\bin\\shutdown.bat"); // 调用外部程序
		final InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null)
			buf.append(line);
		System.out.println("输出结果为：" + buf);
	}

	public void start(String path) throws IOException {
		Process process = Runtime.getRuntime().exec("cmd /c  C:\\Users\\CVTE\\Desktop\\标注demo\\tomcat\\bin\\startup.bat"); // 调用外部程序
		final InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null)
			buf.append(line);
		System.out.println("输出结果为：" + buf);
	}
	
	public void writePro() {
		try{
			String rootPath = System.getProperty("user.dir");
			File f5=new File(rootPath + "/tomcat.bat");
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f5),"UTF-8"); 
			//FileWriter fw=new FileWriter(f5);
	        BufferedWriter bw=new BufferedWriter(osw);
	        bw.write("set  CATALINA_HOME=" + rootPath);
	        bw.newLine();
	        bw.write("wmic ENVIRONMENT create name=\"TOMCAT_HOME\",VariableValue=\"%CATALINA_HOME%\"");
			bw.flush();
	        bw.close();
			osw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
					
		String tomcatPath = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(tomcatPath);
		tomcatPath = tomcatPath + "/Tomcat/bin/startup.bat";
		conTomCat con = new conTomCat();
		con.start(tomcatPath);
		con.writePro();
		}
	}
