package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	//Dependence Injection
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment; 
	
	@FXML
	private TableColumn<Department, Integer> tableViewColumnId; 
	
	@FXML
	private TableColumn<Department, String> tableViewColumnName; 
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onNewBtAction");
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null !");			
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}

	private void initializeNodes() {
		tableViewColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableViewColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		
		//TableView acompanhar tamanho da view principal
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
