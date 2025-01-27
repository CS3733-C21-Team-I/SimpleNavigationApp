package edu.wpi.cs3733.c21.teamI.parking.reservations.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import edu.wpi.cs3733.c21.teamI.parking.reservations.StaffPermit;
import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PermitRequestController {

  @FXML private JFXTextField licensePlateTextfield;
  @FXML private JFXTextField contactNumTextfield;
  @FXML private JFXTextField prefLotTextfield;
  @FXML private JFXDatePicker empParkStartDate;
  @FXML private JFXDatePicker empParkEndDate;
  @FXML private JFXButton empParkSubmit;
  @FXML private JFXButton empParkClear;
  @FXML private Pane currentPermitFrame;
  @FXML private JFXButton deletePermitButton;
  @FXML private Label blockLabel;
  @FXML private Label floorLabel;
  @FXML private Label slotLabel;
  @FXML private JFXCheckBox disabilityStickerCheckbox;

  public void submit(ActionEvent e) {

    Timestamp start = Timestamp.valueOf(empParkStartDate.getValue().atStartOfDay());
    Timestamp end = Timestamp.valueOf(empParkEndDate.getValue().atTime(23, 59));

    ParkingPeripheralServerManager.getInstance()
        .requestStaffPermit(
            ApplicationDataController.getInstance().getLoggedInUser(),
            licensePlateTextfield.getText(),
            contactNumTextfield.getText(),
            start,
            end);

    refreshCurrentPermit();
  }

  public void clear(ActionEvent e) {
    empParkStartDate.setValue(LocalDate.now());
    empParkEndDate.setValue(LocalDate.now().plusDays(30));
    licensePlateTextfield.setText("");
    prefLotTextfield.setText("");
  }

  public void navigate(javafx.event.ActionEvent e) {
    //    ViewManager.navigate(e);
  }

  public void initialize() {
    refreshCurrentPermit();
  }

  private void refreshCurrentPermit() {
    StaffPermit ticket =
        ParkingPeripheralServerManager.getInstance()
            .getStaffPermitForUser(ApplicationDataController.getInstance().getLoggedInUser());

    if (ticket == null) {
      currentPermitFrame.setVisible(false);
      return;
    }

    currentPermitFrame.setVisible(true);
    blockLabel.setText("Block: park block");
    floorLabel.setText("Floor: floor");
    slotLabel.setText("Slot: " + ticket.getSlotId());
  }
}
