package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServiceViewController extends Application {

  @FXML AnchorPane replaceRequest;
  @FXML JFXButton medicineButton;
  //    Label titleLabel = null;
  //    public HomeController homeController;

  @FXML
  public void navigate(ActionEvent e) throws IOException {
    String id = ((JFXButton) e.getSource()).getId();
    replaceRequest.getChildren().clear();
    if (id.equals("medicineButton")) {
      System.out.println("Medicine Button Clicked");
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/MedicineDelivery.fxml")));
    } else if (id.equals("securityButton")) {
      //      titleLabel.setText("Language Service Request");
      System.out.println("Security Button Clicked");
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/SecurityService.fxml")));
    } else if (id.equals("internalButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/InternalTransportation.fxml")));
    } else if (id.equals("maintenanceButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/MaintenanceRequest.fxml")));
    } else if (id.equals("sanitationButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/SanitationRequest.fxml")));
    } else if (id.equals("laundryButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(getClass().getResource("/fxml/serviceRequests/LaundryRequest.fxml")));
    } else if (id.equals("computerButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/ComputerServiceRequest.fxml")));
    } else if (id.equals("audioButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/AudioVisualRequest.fxml")));
    } else if (id.equals("languageButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/LanguageRequest.fxml")));
    } else if (id.equals("religiousButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/serviceRequests/ReligiousRequest.fxml")));
    } else if (id.equals("parkingButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass()
                      .getResource("/fxml/serviceRequests/EmployeeParkingPermitRequest.fxml")));
    } else {
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    System.out.println("service view loaded");
  }
}