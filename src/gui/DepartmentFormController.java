package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangeListerners = new ArrayList<>();

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private Button BtSave;

	@FXML
	private Button BtCancel;

	@FXML
	private Label labelError;

	private Department getFormData() {
		Department obj = new Department();

		obj.setId(Utils.tryParseToInt(textFieldId.getText()));
		obj.setName(textFieldName.getText());

		return obj;

	}

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListerner(DataChangeListener listener){
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
		} catch (DbException e) {
			Alerts.showAlert("ERROR Saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for(DataChangeListener listener : dataChangeListerners) {
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

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(textFieldName, 25);

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Error, entity was null");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
	}

}
