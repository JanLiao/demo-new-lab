package com.cvte.util;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.scene.control.Label;
import javafx.stage.Stage;

/** 
* @author: jan 
* @date: 2018年6月4日 下午3:04:58 
*/
public class DialogUtil {

	@SuppressWarnings("rawtypes")
	public static void showAlert(Stage stage, String msg) {
		JFXAlert alert = new JFXAlert((Stage)stage.getScene().getWindow());
        alert.setOverlayClose(false);
        alert.setSize(320, 160);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("温馨小提示"));
        Label content = new Label(msg);
        layout.setBody(content);
        JFXButton closeButton = new JFXButton("关  闭");
        closeButton.setStyle("-fx-background-color: GREEN;-fx-text-fill: WHITE;-fx-font-size: 15px;-fx-padding: 0.5em 0.50em;");
        closeButton.setOnAction(event -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
	}
	
	@SuppressWarnings("rawtypes")
	public static void exportShow(Stage stage, String id) {
		JFXAlert alert = new JFXAlert((Stage)stage.getScene().getWindow());
        alert.setOverlayClose(false);
        alert.setSize(320, 160);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("温馨小提示"));
        Label content = new Label("导出数据操作");
        content.setStyle("-fx-text-fill: rgb(77,102,204);-fx-font-size: 18px;");
        layout.setBody(content);
        JFXButton closeButton = new JFXButton("导   出");
        closeButton.setStyle("-fx-background-color: GREEN;-fx-text-fill: WHITE;-fx-font-size: 15px;-fx-padding: 0.5em 0.50em;-fx-pref-width: 110;");
        closeButton.setOnAction(event -> {
        	alert.hideWithAnimation();
        	ExportUtil.exportData(id);
        });
        JFXButton clsButton = new JFXButton("取  消");
        clsButton.setStyle("-fx-background-color: RED;-fx-text-fill: WHITE;-fx-font-size: 15px;-fx-padding: 0.5em 0.50em;-fx-pref-width: 110;");
        clsButton.setOnAction(event -> {
        	alert.hideWithAnimation();
        });
        layout.setActions(closeButton, clsButton);
        alert.setContent(layout);
        alert.show();
	}
	
}
