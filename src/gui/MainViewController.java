package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sql.rowset.spi.SyncResolver;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onAboutAction() {
		loadView("/gui/about.fxml");
	}
	
	@FXML
	public void onSellerAction() {
		System.out.println("Seller");
	}
	@FXML
	public void onDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	private synchronized void loadView(String absolutName) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
		VBox newVbox = loader.load();
		Scene mainScene = Main.getMainScene();
		
		VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
		Node mainMenu = mainVbox.getChildren().get(0);
		mainVbox.getChildren().clear();
		mainVbox.getChildren().add(mainMenu);
		mainVbox.getChildren().addAll(newVbox);
		
		
		}catch(IOException e) {
			Alerts.showAlert("IOException", "Error", e.getMessage(), AlertType.ERROR);
		}
	}

}
