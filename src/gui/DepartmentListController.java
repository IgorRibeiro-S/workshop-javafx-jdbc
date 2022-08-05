package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// Dependence Injection
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Integer> tableViewColumnId;

	@FXML
	private TableColumn<Department, String> tableViewColumnName;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		// Pegando o stage atual para entrar com o modal
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
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
		initEditButtons();
	}

	private void initializeNodes() {
		tableViewColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableViewColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// TableView acompanhar tamanho da view principal
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(Department obj, String absolutName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListerner(this);
			controller.updateFormData();

			// Instanciar novo stage (sobrepor a tela)
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane)); // Instanciar nova cena pelo parametro pane.
			dialogStage.setResizable(false); // Redimensionamento de tela
			dialogStage.initOwner(parentStage); // Stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// janela do tipo modal
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

}
