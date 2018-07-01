package com.cvte.tmp;

//import com.cvte.ui.JavaApp;

import javafx.application.Application;
import javafx.stage.Stage;

/** 
* @author: jan 
* @date: 2018年5月8日 上午10:52:38 
*/
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
 
public class WebViewSample extends Application {
 
    private Scene scene;
 
    @Override
    public void start(Stage stage) {
        // create scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser(), 1050, 600, Color.web("#4169E1"));
        stage.setScene(scene);
        // apply CSS style
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        // show stage
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
 
class Browser extends Region {
 
    private HBox toolBar;
    private JavaApp c;
//    private static String[] imageFiles = new String[]{
//        "ic_cvte1.png",
//        "ic_cvte1.png",
//        "ic_cvte1.png",
//        "ic_cvte1.png",
//        "ic_cvte1.png"
//    };
//    private static String[] captions = new String[]{
//        "Products",
//        "Blogs",
//        "Documentation",
//        "Partners",
//        "Help"
//    };
//    private static String[] urls = new String[]{
//        "http://www.oracle.com/products/index.html",
//        "http://blogs.oracle.com/",
//        "http://docs.oracle.com/javase/index.html",
//        "http://localhost:8080/Glaucoma/",
//        //"http://www.oracle.com/partners/index.html",
//        WebViewSample.class.getResource("../views/login.html").toExternalForm()
//        //"http://172.17.82.212:8601/login"
//    };
    //final ImageView selectedImage = new ImageView();
    //final Hyperlink[] hpls = new Hyperlink[captions.length];
    //final Image[] images = new Image[imageFiles.length];
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    final Button showPrevDoc = new Button("Toggle Previous Docs");
    private boolean needDocumentationButton = false;
 
    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
 
//        for (int i = 0; i < captions.length; i++) {
//            // create hyperlinks
//            Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
//            Image image = images[i] =
//                    new Image(getClass().getResourceAsStream(imageFiles[i]));
//            hpl.setGraphic(new ImageView(image));
//            final String url = urls[i];
//            final boolean addButton = (hpl.getText().equals("Documentation"));
// 
//            // process event 
//            hpl.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent e) {
//                    needDocumentationButton = addButton;
//                    webEngine.load(url);
//                }
//            });
//        }
 
       
        // create the toolbar
        toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.getStyleClass().add("browser-toolbar");
        //toolBar.getChildren().addAll(hpls);
        toolBar.getChildren().add(createSpacer());
 
        //set action for the button
        showPrevDoc.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                webEngine.executeScript("toggleDisplay('PrevRel')");
            }           
        });
 
        // process page loading
        //webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue<? extends State> ov,
                    State oldState, State newState) {
                    toolBar.getChildren().remove(showPrevDoc); 
                    System.out.println("new = " + newState);
                    if (newState == State.SUCCEEDED) {
                            JSObject win = 
                                (JSObject) webEngine.executeScript("window");
                            c = new JavaApp();
							win.setMember("app", c);
							//Javascript与Java交互 比如10秒后，再调用Java方法发现没有反应了  可能匿名对象被gc掉了导致
							//win.setMember("app", new JavaApp());
                            if (needDocumentationButton) {
                                toolBar.getChildren().add(showPrevDoc);
                            }
                        }
                    }
                }
        );
 
        // load the home page        
        webEngine.load(WebViewSample.class.getResource("../views/login.html").toExternalForm());
        browser.setZoom(1);
 
        //add components
        getChildren().add(toolBar);
        getChildren().add(browser);
    }
 
    // JavaScript interface object
    public class JavaApp {
 
        public void exit(String value) {
        	System.out.println("value = " + value);
            //Platform.exit();
        }
        
        public void alig() {
        	System.out.println("9999999");
        }
        
        public void loadData(){
        	System.out.println(9999);
        }
        
        public void login(String account, String password) {
        	System.out.println(account + " = " + password);
        	if(Integer.parseInt(account) == 1) {
        		webEngine.load(WebViewSample.class.getResource("../views/index.html").toExternalForm());
        	}else if(Integer.parseInt(account) == 2) {
        		webEngine.load("http://www.oracle.com/products/index.html");
        	}else if(Integer.parseInt(account) == 3) {
        		webEngine.load("http://docs.oracle.com");
        	}
        }
    }
 
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        double tbHeight = toolBar.prefHeight(w);
        layoutInArea(browser,0,0,w,h-tbHeight,0,HPos.CENTER, VPos.CENTER);
        layoutInArea(toolBar,0,h-tbHeight,w,tbHeight,0,HPos.CENTER,VPos.CENTER);
    }
 
    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }
}
