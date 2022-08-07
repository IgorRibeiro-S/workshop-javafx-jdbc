package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	private SellerService service;
	private DepartmentService depService;
	private List<DataChangeListener> dataChangeListerners = new ArrayList<>();

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private TextField textFieldEmail;

	@FXML
	private DatePicker textFieldBirthDate;

	@FXML
	private TextField textFieldBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Button BtSave;

	@FXML
	private Button BtCancel;

	@FXML
	private ObservableList<Department> obsList;

	@FXML
	private Label labelError;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException validationException = new ValidationException("Error Validation");

		obj.setId(Utils.tryParseToInt(textFieldId.getText()));

		if (textFieldName.getText() == null || textFieldName.getText().trim().equals("")) {
			validationException.addErrors("name", "Field can't be empty");
		}
		obj.setName(textFieldName.getText());

		if (validationException.getErrors().size() > 0) {
			throw validationException;
		}

		return obj;

	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService depService) {
		this.service = service;
		this.depService = depService;
	}

	public void subscribeDataChangeListerner(DataChangeListener listener) {
		dataChangeListerners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Entity was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Alerts.showAlert("Save", null, "SAVE SUCESS", AlertType.INFORMATION);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("ERROR Saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListerners) {
			listener.onDataChanged();
		}

	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		Constraints.setTextFieldMaxLength(textFieldName, 45);
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 40);
		Constraints.setTextFieldMaxLength(textFieldBaseSalary, 10);
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Error, entity was null");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
		textFieldEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		textFieldBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			textFieldBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
			;
		}
		if(entity.getDeparment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else {
		comboBoxDepartment.setValue(entity.getDeparment());
		}
	}

	public void loadAssocietedObjects() {
		List<Department> list = depService.findAll();
		if (depService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("name")) {
			labelError.setText(errors.get("name"));
		}

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
