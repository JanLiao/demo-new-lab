package com.cvte.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/** 
* @author: jan 
* @date: 2018年5月8日 下午2:40:12 
*/
public class WebUI extends Application {
	
	private Scene scene;
	public static Stage stageOld;

	@Override
	public void start(Stage stage) throws Exception {
		stageOld = stage;
		stage.setTitle("CVTE医疗图片标注平台");
		scene = new Scene(new Browser(), 1120, 640, Color.web("#4169E1"));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("cvte.png")));
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

//scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//
//	@Override
//	public void handle(MouseEvent e) {
//		MouseButton button = e.getButton();
//		switch (button) {
//		case SECONDARY:
//			System.out.println("Right Button Pressed");
//			e.consume();
//			
//		default:
//			System.out.println(button);
//		}
//	}
//	
//});
//scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//	@Override
//		public void handle(MouseEvent e) {
//			MouseButton button = e.getButton();
//			switch (button) {
//			case PRIMARY:
//				System.out.println("Left Button Pressed");
//				break;
//			case SECONDARY:
//				System.out.println("Right Button Pressed");
//				break;
//			case MIDDLE:
//				System.out.println("Middle Button Pressed");
//				break;
//			default:
//				System.out.println(button);
//			}
//		}
//	
//});
