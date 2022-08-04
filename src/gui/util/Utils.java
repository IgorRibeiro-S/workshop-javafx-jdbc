package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Department;

public class Utils {
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();

	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);

		} catch (NumberFormatException e) {
			return null;
		}
	}
}
