package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Collection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServiceViewController extends Application {

  @FXML AnchorPane replaceRequest;
  @FXML JFXButton medicineButton;
  //    Label titleLabel = null;
  //    public HomeController homeController;

  @FXML
  public void navigate(MouseEvent e) throws IOException {
    String id = ((JFXButton) e.getSource()).getId();
    if (id.equals("medicineButton")) {
      System.out.println(getClass().getResource("/fxml/menuFiles/MedicineDelivery.fxml"));
      System.out.println("Medicine Button Clicked");
      FXMLLoader profLoader =
          new FXMLLoader(
              getClass().getClassLoader().getResource("/fxml/menuFiles/MedicineDelivery.fxml"));
      profLoader.setLocation(getClass().getResource("/fxml/menuFiles/MedicineDelivery.fxml"));
      replaceRequest.getChildren().setAll((Collection<? extends Node>) profLoader.load());
    } else if (id.equals("securityButton")) {
      //      titleLabel.setText("Language Service Request");
      System.out.println("Security Button Clicked");
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/SecurityService.fxml")));
    } else if (id.equals("internalButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/menuFiles/InternalTransportation.fxml")));
    } else if (id.equals("maintenanceButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/MaintenanceRequest.fxml")));
    } else if (id.equals("sanitationButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/SanitationRequest.fxml")));
    } else if (id.equals("laundryButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/LaundryRequest.fxml")));
    } else if (id.equals("computerButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/menuFiles/ComputerServiceRequest.fxml")));
    } else if (id.equals("audioButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/AudioVisualRequest.fxml")));
    } else if (id.equals("languageButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/LanguageRequest.fxml")));
    } else if (id.equals("religiousButton")) {
      replaceRequest
          .getChildren()
          .add(FXMLLoader.load(getClass().getResource("/fxml/menuFiles/ReligiousRequest.fxml")));
    } else if (id.equals("parkingButton")) {
      replaceRequest
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass().getResource("/fxml/menuFiles/EmployeeParkingPermitRequest.fxml")));
    } else {
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
