package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.CovidTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class COVIDTrackerController {

  @FXML private Label soreThroat, breath, taste, fever, congestion, cough, nausea;
  @FXML private Label diarrhea, headache, positiveContact, sympContact, testResult;
  @FXML private Button risk, notRisk;
  CovidTicket covidTicket;
  @FXML Label finish;

  private void color(boolean test, Label label) {
    if (test) {
      label.setStyle("-fx-background-color: #e34b4b");
    } else {
      label.setStyle("-fx-background-color: #4ccf70");
    }
  }

  @FXML
  public void initialize() {
    covidTicket =
        (CovidTicket)
            ServiceTicketDatabaseManager.getInstance().getServiceTicketDB().stream()
                .filter(
                    t ->
                        t.getTicketType() == ServiceTicket.TicketType.COVID
                            && t.getAssignedUserID()
                                .contains(
                                    ApplicationDataController.getInstance()
                                        .getLoggedInUser()
                                        .getUserId()))
                .findFirst()
                .orElse(null);
    System.out.println(ApplicationDataController.getInstance().getLoggedInUser().getUserId());
    System.out.println(
        ServiceTicketDatabaseManager.getInstance().getServiceTicketDB().stream()
            .filter(t -> t.getTicketType() == ServiceTicket.TicketType.COVID)
            .collect(Collectors.toList()));
    if (covidTicket == null) {
      System.out.println("User has no covid ticket assigned");
    } else {
      color(covidTicket.isSoreThroat(), soreThroat);
      color(covidTicket.isBreathing(), breath);
      color(covidTicket.isTasteSmellLoss(), taste);
      color(covidTicket.isFever(), fever);
      color(covidTicket.isCongestion(), congestion);
      color(covidTicket.isCough(), cough);
      color(covidTicket.isNausea(), nausea);
      color(covidTicket.isDiarrhea(), diarrhea);
      color(covidTicket.isHeadache(), headache);
      color(covidTicket.isContactCovidConfirmed(), positiveContact);
      color(covidTicket.isContactCovidSymptoms(), sympContact);
      color(covidTicket.isCovidTest(), testResult);
    }
  }

  public void submit(ActionEvent actionEvent) {
    UserDatabaseManager.getInstance()
        .updateCovidRiskForUser(
            covidTicket.getRequestingUserID(),
            (actionEvent.getSource() == risk)
                ? User.CovidRisk.COVID_RISK
                : User.CovidRisk.NO_COVID_RISK);
    System.out.println(
        UserDatabaseManager.getInstance().getCovidRiskForUser(covidTicket.getRequestingUserID()));
    ServiceTicketDatabaseManager.getInstance().updateTicket(covidTicket.getTicketId());
    risk.setDisable(true);
    notRisk.setDisable(true);
    finish.setVisible(true);
  }
}
