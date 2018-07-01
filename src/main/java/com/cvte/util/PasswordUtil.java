package com.cvte.util;

import com.cvte.cons.Password;
import com.cvte.ui.Browser;

/** 
* @author: jan 
* @date: 2018年6月4日 上午1:15:56 
*/
public class PasswordUtil {

	public static void checkPsw() {
		PropertyUtil prop = new PropertyUtil();
		prop.loadProperty();
		System.out.println(Password.User);
		if("1".equals(PropertyUtil.Rem)) {
			String[] userAll = Password.User.split(",");
			String[] pswAll = Password.AllPSW.split(",");
			String user = PropertyUtil.User;
			String psw = "";
			for(int i = 0; i < userAll.length; i++) {
				if(user.equals(userAll[i].trim())) {
					psw = pswAll[i].trim();
					break;
				}
			}
			System.out.println(user + "=" + psw);
			Browser.webEngine.executeScript("setUserPsw('"+ user + "','" + psw.trim() + "')");
		}
	}

}
