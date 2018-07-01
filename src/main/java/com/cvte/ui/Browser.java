package com.cvte.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSObject;

/** 
* @author: jan 
* @date: 2018年5月8日 下午2:44:56 
*/
public class Browser extends Region {

	public static WebView browser = new WebView();
	public static WebEngine webEngine = browser.getEngine();
	private JavaApp js = new JavaApp();
	
	public Browser() {
		//指定是否启用上下文菜单。(禁用WebView的弹出菜单)禁用右键弹出菜单
		//browser.setContextMenuEnabled(false);

		webEngine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
			@Override
			public WebEngine call(PopupFeatures config) {
				return new WebView().getEngine();
			}
		});
		
		//process page loading(加载页面)
		webEngine.getLoadWorker().stateProperty().addListener(
				new ChangeListener<State>() {

					@Override
					public void changed(ObservableValue<? extends State> ov, 
							State oldState, State newState) {
						//System.out.println("new = " + newState);
						if(newState == State.SUCCEEDED) {
							JSObject win = (JSObject) webEngine.executeScript("window");
							//JavaApp js = new JavaApp();
							win.setMember("app", js);
							//Javascript与Java交互 比如10秒后，再调用Java方法发现没有反应了  可能匿名对象被gc掉了导致
							//win.setMember("app", new JavaApp());
						}
					}
					
				}
		);
		
		String rootPath = Browser.class.getResource("").getPath();
		//String rootPath = JavaApp.class.getResource("/").getPath().replaceAll("%20", "");
		rootPath = rootPath.substring(0, rootPath.length() - 1);
		String[] str = rootPath.split("/");
		//login the home page
		webEngine.load(this.getClass().getResource("login.html").toExternalForm());
		
		//add components
		getChildren().add(browser);
	}
	
//	// JavaScript interface object
//	public class JavaApp {
//		
//		//login登录
//		public void login(String account, String password) {
//			System.out.println("登录 = " + account + " , " + password);
//		}
//		
//		//关闭界面
//		public void exit() {
//			Platform.exit();
//		}
//	}
	
//	private Node createSpacer() {
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        return spacer;
//    }
 
    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        System.out.println("w = " + w + ", h = " + h);
        layoutInArea(browser,0,0,w,h,0,HPos.CENTER, VPos.CENTER);
    }
 
    @Override
    protected double computePrefWidth(double height) {
    	System.out.println("height = 1050");
        return 1050;
    }
 
    @Override
    protected double computePrefHeight(double width) {
    	System.out.println("width = 600");
        return 600;
    }
}
