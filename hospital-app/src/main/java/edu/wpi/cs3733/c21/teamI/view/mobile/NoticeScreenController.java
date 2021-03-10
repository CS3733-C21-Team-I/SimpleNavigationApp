package edu.wpi.cs3733.c21.teamI.view.mobile;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class NoticeScreenController {

  @FXML JFXButton okBtn;
  @FXML StackPane root;

  @FXML
  public void initialize() {
    changeCovidText(
        UserDatabaseManager.getInstance()
                .getCovidRiskForUser(
                    ApplicationDataController.getInstance().getLoggedInUser().getUserId())
            == User.CovidRisk.COVID_RISK);
  }

  @FXML
  public void goToPathfinding() throws IOException {
    root.getChildren().clear();
    root.getChildren()
        .add(FXMLLoader.load(getClass().getResource("/fxml/mobilePages/PathfindingMobile.fxml")));
  }

  public void changeCovidText(boolean covidRisk) {
    Label label2 = (Label) root.lookup("#label2");
    Label label3 = (Label) root.lookup("#label3");
    if (!covidRisk) {
      label2.setText("You have not been designated as a potential COVID-19 risk.");
      label2.setTextFill(Color.BLACK);
      label3.setText(
          "You can enter the hospital now and navigate to your destination through the pathfinding interface.");
    }
  }

  @FXML
  public void exit(MouseEvent e) {
    Stage stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
