package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.Controllers;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.MapEditManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileController extends Application {
	MapEditManager mapManager;

	//ticket view population on init
	@FXML
	VBox requestContainer, loginVBox, serviceDisplay;
	@FXML
	TextField username;
	@FXML
	PasswordField password;
	@FXML
	Label headerLabel;
	public String uName;
	public static String pass;

	private void populateTicketsProfile() {
		Group root = mapManager.getRoot();
		if (ApplicationDataController.getInstance()
				.getLoggedInUser()
				.hasPermission(User.Permission.VIEW_TICKET)) {
			generateRequestList();
			root.lookup("#loginVBox").setVisible(false);
			root.lookup("#serviceDisplay").setVisible(true);
		} else {
			root.lookup("#loginVBox").setVisible(true);
			root.lookup("#serviceDisplay").setVisible(false);
		}
	}

	private void setupMapViewHandlers() {
		Group root = mapManager.getRoot();
		((ListView) root.lookup("#startList"))
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(ChangeListener<String>)
								(ov, oldVal, newVal) -> {
									((TextField) root.lookup("#start")).setText(newVal);
									root.lookup("#startList").setVisible(false);
								});
		((ListView) root.lookup("#destList"))
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(ChangeListener<String>)
								(ov, oldVal, newVal) -> {
									((TextField) root.lookup("#destination")).setText(newVal);
									root.lookup("#destList").setVisible(false);
								});
		root.setOnMouseClicked(
				(MouseEvent evt) -> {
					if (root.lookup("#mapPane") != null) {
						root.lookup("#startList").setVisible(false);
						root.lookup("#destList").setVisible(false);
					}
				});
	}

	@FXML
	public void login() {
		uName = username.getText();
		pass = password.getText();
		if (ApplicationDataController.getInstance().logInUser(uName, pass)) {
			loginVBox.setVisible(false);
			serviceDisplay.setVisible(true);
			headerLabel.setText("You successfully logged in.");
			System.out.println(uName + ' ' + pass);
			if (ApplicationDataController.getInstance()
					.getLoggedInUser()
					.hasPermission(User.Permission.VIEW_TICKET)) {
				generateRequestList();
			}
		} else {
			headerLabel.setText("Error: Invalid login.");
		}
	}

	public void generateRequestList() {
		System.out.println(mapManager.getRoot());
		List<ServiceTicket> requests =
				ServiceTicketDatabaseManager.getInstance()
						.getTicketsForRequestId(
								ApplicationDataController.getInstance().getLoggedInUser().getUserId());
		List<String> requestNames =
				requests.stream()
						.map(st -> st.getTicketType() + " #" + st.getTicketId())
						.collect(Collectors.toList());
		VBox container =
				(VBox)
						((ScrollPane)
								mapManager.getRoot().lookup("#serviceDisplay").lookup("#requestScrollPane"))
								.getContent()
								.lookup("#requestContainer");
		container.getStylesheets().add("/fxml/fxmlResources/main.css");
		for (int i = 0; i < requestNames.size(); i++) {
			Button requestButton = new Button(requestNames.get(i));
			System.out.println(requestButton.getText());
			int finalI = i;
			requestButton.setOnAction(
					event -> {
						try {
							mapManager.startRequestView(requests.get(finalI));
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
			requestButton.getStyleClass().add("requestButton");
			requestButton.setMinHeight(50);
			requestButton.setMaxWidth(container.getMaxWidth());
			container.getChildren().add(requestButton);
		}
		mapManager.getRoot().lookup("#serviceDisplay").lookup("#requestScrollPane").setVisible(true);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
