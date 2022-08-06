package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	private SellerService service;
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

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException validationException = new ValidationException("Error Validation");
		
		obj.setId(Utils.tryParseToInt(textFieldId.getText()));
		
		if(textFieldName.getText() == null ||textFieldName.getText().trim().equals("")) {
			validationException.addErrors("name", "Field can't be empty");
		}
		obj.setName(textFieldName.getText());
		
		if(validationException.getErrors().size() > 0) {
			throw validationException;
		}

		return obj;

	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService service) {
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
		}catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
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
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("name")) {
			labelError.setText(errors.get("name"));
		}
		
		
	}

}
