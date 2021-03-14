package edu.wpi.cs3733.c21.teamI.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class KioskController {
  @FXML private JFXRippler min10, min30, hour1, hour2, hour3, allDay, overnight;
  @FXML private Label parkingSlot, start, end, price, barCode, ticketID;
  @FXML private JFXButton print;

  public void printTicket() {
    System.out.println("Print ticket");
  }

  @FXML
  public void redrawTicket(MouseEvent e) {
    System.out.println(e.getSource());
    String priceString = "";
    if (e.getSource() == min10) {
      priceString = "1.00";
    } else if (e.getSource() == min30) {
      priceString = "1.50";
    } else if (e.getSource() == hour1) {
      priceString = "2.00";
    } else if (e.getSource() == hour2) {
      priceString = "3.00";
    } else if (e.getSource() == hour3) {
      priceString = "4.00";
    } else if (e.getSource() == allDay) {
      priceString = "8.00";
    } else if (e.getSource() == overnight) {
      priceString = "12.00";
    }
    price.setText("Price: $" + priceString);
  }
}
