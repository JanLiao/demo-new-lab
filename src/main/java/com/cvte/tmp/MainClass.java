package com.cvte.tmp;
/** 
* @author: jan 
* @date: 2018年5月8日 上午9:44:40 
*/
import java.awt.BorderLayout;
import java.awt.Toolkit;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.JFrame;

import com.cvte.tmp.Browser.JavaApp;

public class MainClass {
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 600;
	//private static final String url = "http://172.17.82.212:8601/label";
	//private static final String url = "http://172.17.195.68:8099/Glaucoma";
	private static final String urlStart = "http://";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		final JFXPanel webBrowser = new JFXPanel();
		frame.setLayout(new BorderLayout());
		frame.add(webBrowser, BorderLayout.CENTER);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Group root = new Group();
				Scene scene = new Scene(root, WIDTH, HEIGHT);
				webBrowser.setScene(scene);
				Double widthDouble = new Integer(WIDTH).doubleValue();
				Double heightDouble = new Integer(HEIGHT).doubleValue();

				VBox box = new VBox(10);
				HBox urlBox = new HBox(10);
				final TextField urlTextField = new TextField();
				urlTextField.setText("");
				Button go = new Button("go");
				urlTextField.setPrefWidth(WIDTH - 70);
				urlBox.getChildren().addAll(urlTextField, go);

				WebView view = new WebView();
				view.setMinSize(widthDouble, heightDouble);
				view.setPrefSize(widthDouble, heightDouble);
				final WebEngine eng = view.getEngine();
//				eng.getLoadWorker().stateProperty().addListener(
//			            new ChangeListener<State>() {
//			                @Override
//			                public void changed(ObservableValue<? extends State> ov,
//			                    State oldState, State newState) {
//			                    System.out.println("new = " + newState);
//			                    if (newState == State.SUCCEEDED) {
//			                            JSObject win = 
//			                                (JSObject) eng.executeScript("window");
//			                            win.setMember("app", new JavaApp());
//			                            
//			                        }
//			                    }
//			                }
//			        );
				eng.load(MainClass.class.getResource("../views/login.html").toExternalForm());
				root.getChildren().add(view);

				box.getChildren().add(urlBox);
				box.getChildren().add(view);
				root.getChildren().add(box);

				go.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (!urlTextField.getText().startsWith(urlStart)) {
							eng.load(urlStart + urlTextField.getText());
						} else {
							eng.load(urlTextField.getText());
						}
					}
				});
			}
		});
		
		

		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocation((screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2);
		frame.setVisible(true);
	}

}
