package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceFormController extends Application {
	MapEditManager mapManager;

	//setup request location handlers
	@FXML
	TextField sanLocation;
	ServiceTicket sanitationTicket;
	ServiceTicket maintenanceTicket;

	@FXML
	CheckBox sanEmergency;
	@FXML
	TextArea sanDescription;
	@FXML TextField sanAssignedID;
	@FXML
	HBox sanRequestType;
	@FXML TextField sanRequestID;

	@FXML TextField mainLocation;
	@FXML TextArea mainDesc;
	@FXML CheckBox mainEmerg;
	@FXML
	MenuButton mainRequestType;
	@FXML TextField mainAssignedID;
	@FXML TextField mainRequestID;

	@FXML
	public void createSanitationTicket(ActionEvent e) {
		try {
			int RequestID = Integer.parseInt(sanRequestID.getText());
			int AssignedID = Integer.parseInt(sanAssignedID.getText());
			sanitationTicket =
					new ServiceTicket(
							RequestID,
							AssignedID,
							ServiceTicket.TicketType.SANITATION,
							sanLocation.getText(),
							sanDescription.getText(),
							sanEmergency.isSelected(),
							false);

			ServiceTicketDatabaseManager.getInstance().addTicket(sanitationTicket);
		} catch (Exception o) {
			System.out.println("Error" + o);
		}
	}

	@FXML
	public void createMaintenanceTicket(ActionEvent o) {
		try {
			int RequestID = Integer.parseInt(mainRequestID.getText());
			System.out.println(RequestID);
			int AssignID = Integer.parseInt(mainAssignedID.getText());
			System.out.println(AssignID);

			maintenanceTicket =
					new ServiceTicket(
							RequestID,
							AssignID,
							ServiceTicket.TicketType.MAINTENANCE,
							mainLocation.getText(),
							mainDesc.getText(),
							mainEmerg.isSelected(),
							false);
			System.out.println(maintenanceTicket);
			//  ServiceTicketDatabaseManager.getInstance().addTicket(maintenanceTicket);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" Error " + e);
		}
	}

	public void navigate(ActionEvent e) throws IOException {
		ViewManager.navigate(e);}

	private void setupRequestView() {
		Group root = mapManager.getRoot();
		((ListView) root.lookup("#serviceLocationList"))
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(ChangeListener<String>)
								(ov, oldVal, newVal) -> {
									((TextField) root.lookup("#requestLocation")).setText(newVal);
									root.lookup("#serviceLocationList").setVisible(false);
								});
		root.setOnMouseClicked(
				(MouseEvent evt) -> {
					root.lookup("#serviceLocationList").setVisible(false);
				});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
