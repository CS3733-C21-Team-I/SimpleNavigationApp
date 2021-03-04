package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FeedbackViewController {
	@FXML
	AnchorPane replaceRequest;

	@FXML
	public void navigate(ActionEvent e) throws IOException {
		String id = ((JFXButton) e.getSource()).getId();
		replaceRequest.getChildren().clear();
		if (id.equals("religiousButton")) {
			replaceRequest
					.getChildren()
					.add(
							FXMLLoader.load(
									getClass().getResource("/fxml/serviceRequests/MedicineDelivery.fxml")));
		}
	}
}
