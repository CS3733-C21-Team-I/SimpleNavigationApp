package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.ParkingSlip;
import edu.wpi.cs3733.c21.teamI.parking.reservations.PeripheralSlipManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class KioskController {
  @FXML private JFXRippler min10, min30, hour1, hour2, hour3, allDay, overnight;
  @FXML private Label parkingSlot, start, end, price, barCode, ticketID;
  @FXML private JFXButton print;
  @FXML private StackPane ticketPane;

  private int minutes;

  @FXML
  public void initialize() {
    ticketPane.setVisible(false);
  }

  public void printTicket() {
    ParkingSlip slip =
        ParkingPeripheralServerManager.getInstance()
            .createNewSlip(
                new Timestamp(System.currentTimeMillis()),
                minutes,
                (int) Double.parseDouble(price.getText().substring(8)) * 100);
    PeripheralSlipManager.getInstance().addTicket(slip);
  }

  @FXML
  public void redrawTicket(MouseEvent e) {
    System.out.println(e.getSource());
    String priceString = "";
    if (e.getSource() == min10) {
      minutes = 10;
      priceString = "1.00";
    } else if (e.getSource() == min30) {
      minutes = 30;
      priceString = "1.50";
    } else if (e.getSource() == hour1) {
      minutes = 60;
      priceString = "2.00";
    } else if (e.getSource() == hour2) {
      minutes = 120;
      priceString = "3.00";
    } else if (e.getSource() == hour3) {
      minutes = 180;
      priceString = "4.00";
    } else if (e.getSource() == allDay) {
      minutes =
          (int)
              LocalDateTime.now()
                  .until(
                      LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(17),
                      ChronoUnit.MINUTES);
      priceString = "8.00";
    } else if (e.getSource() == overnight) {
      minutes =
          (int)
              LocalDateTime.now()
                  .until(
                      LocalDateTime.now().toLocalDate().atStartOfDay().plusHours(36),
                      ChronoUnit.MINUTES);
      priceString = "12.00";
    }
    price.setText("Price: $" + priceString);
  }
}
