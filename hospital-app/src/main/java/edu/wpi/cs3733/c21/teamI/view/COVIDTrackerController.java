package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.CovidTicket;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
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
                .filter(t -> t.getTicketType() == ServiceTicket.TicketType.COVID)
                .findFirst()
                .orElse(null);
    if (covidTicket == null) {
      System.out.println("User has no covid ticket assigned");
    }
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

  public void submit(ActionEvent actionEvent) {
    if (actionEvent.getSource() == risk) {
      // set risk or not risk
      System.out.println("risk");
      System.out.println(
          UserDatabaseManager.getInstance()
              .getUserForScreenname(
                  UserDatabaseManager.getInstance()
                      .getDisplayNameForId(covidTicket.getRequestingUserID())));
    } else {
      System.out.println("no risk");
    }
    ServiceTicketDatabaseManager.getInstance().updateTicket(covidTicket.getTicketId());
    risk.setDisable(true);
    notRisk.setDisable(true);
    finish.setVisible(true);
  }
}
