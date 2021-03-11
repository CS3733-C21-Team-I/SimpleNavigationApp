package edu.wpi.cs3733.c21.teamI.ticket;

import static edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket.TicketType.*;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.c21.teamI.ticket.ticketTypes.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;

public class UniqueColumnFactory {

  public static ArrayList<JFXTreeTableColumn<ServiceTicket, String>> getColumns(
      JFXTreeTableView<ServiceTicket> treeView, ServiceTicket ticket) {
    ArrayList<JFXTreeTableColumn<ServiceTicket, String>> columnList = new ArrayList();
    ServiceTicket.TicketType type = ticket.getTicketType();
    if (type == AUDIO_VISUAL) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Patient Name");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(AudioVisualTicket.class))
                  ? new SimpleStringProperty(
                      ((AudioVisualTicket) param.getValue().getValue()).getPatientName())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Media Type");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(AudioVisualTicket.class))
                  ? new SimpleStringProperty(
                      ((AudioVisualTicket) param.getValue().getValue()).getMediaType())
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2));
    } else if (type == COMPUTER) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Computer Type");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ComputerTicket.class))
                  ? new SimpleStringProperty(
                      ((ComputerTicket) param.getValue().getValue()).getComputerType())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Urgency");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ComputerTicket.class))
                  ? new SimpleStringProperty(
                      Boolean.toString(((ComputerTicket) param.getValue().getValue()).isUrgency()))
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2));
    } else if (type == LANGUAGE) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Language");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LanguageTicket.class))
                  ? new SimpleStringProperty(
                      ((LanguageTicket) param.getValue().getValue()).getLanguage())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Meeting Time");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LanguageTicket.class))
                  ? new SimpleStringProperty(
                      ((LanguageTicket) param.getValue().getValue()).getMeetingTime())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Involves Legal");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LanguageTicket.class))
                  ? new SimpleStringProperty(
                      Boolean.toString(
                          ((LanguageTicket) param.getValue().getValue()).isLegalDocs()))
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3));
    } else if (type == FLORAL) {

      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Patient Name");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(FloralTicket.class))
                  ? new SimpleStringProperty(
                      ((FloralTicket) param.getValue().getValue()).getPatientName())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Delivery Date");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(FloralTicket.class))
                  ? new SimpleStringProperty(
                      ((FloralTicket) param.getValue().getValue()).getDeliveryDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Delivery Time");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(FloralTicket.class))
                  ? new SimpleStringProperty(
                      ((FloralTicket) param.getValue().getValue()).getDeliveryTime())
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3));

    } else if (type == INTERNAL_TRANSPORTATION) {
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Pick Up Date");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(InternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((InternalTransportationTicket) param.getValue().getValue()).getPickUpDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Pick Up Time");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(InternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((InternalTransportationTicket) param.getValue().getValue()).getPickUpTime())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Emergency");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(InternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      Boolean.toString(
                          ((InternalTransportationTicket) param.getValue().getValue())
                              .isEmergency()))
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col4 = makeColumn("Transportation Type");
      col4.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(InternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((InternalTransportationTicket) param.getValue().getValue()).isStretcher()
                          ? "Stretcher"
                          : "Wheelchair")
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col2, col3, col1, col4));
    } else if (type == EXTERNAL_TRANSPORTATION) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Pick Up Date");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ExternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((ExternalTransportationTicket) param.getValue().getValue()).getPickUpDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Pick Up Time");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ExternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((ExternalTransportationTicket) param.getValue().getValue()).getPickUpTime())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Destination");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ExternalTransportationTicket.class))
                  ? new SimpleStringProperty(
                      ((ExternalTransportationTicket) param.getValue().getValue()).getDestination())
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3));
    } else if (type == LAUNDRY) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Pick Up Date");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LaundryTicket.class))
                  ? new SimpleStringProperty(
                      ((LaundryTicket) param.getValue().getValue()).getPickUpDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Pick Up Time");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LaundryTicket.class))
                  ? new SimpleStringProperty(
                      ((LaundryTicket) param.getValue().getValue()).getPickUpTime())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Dry Clean");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(LaundryTicket.class))
                  ? new SimpleStringProperty(
                      Boolean.toString(((LaundryTicket) param.getValue().getValue()).isDryClean()))
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3));
    } else if (type == MEDICINE) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Patient Name");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(MedicineTicket.class))
                  ? new SimpleStringProperty(
                      ((MedicineTicket) param.getValue().getValue()).getPatientName())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Date");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(MedicineTicket.class))
                  ? new SimpleStringProperty(
                      ((MedicineTicket) param.getValue().getValue()).getDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Time");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(MedicineTicket.class))
                  ? new SimpleStringProperty(
                      ((MedicineTicket) param.getValue().getValue()).getTime())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col4 = makeColumn("Drug Name");
      col4.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(MedicineTicket.class))
                  ? new SimpleStringProperty(
                      ((MedicineTicket) param.getValue().getValue()).getDrugName())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col5 = makeColumn("Dose");
      col5.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(MedicineTicket.class))
                  ? new SimpleStringProperty(
                      ((MedicineTicket) param.getValue().getValue()).getDose())
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3, col4, col5));
    } else if (type == RELIGIOUS) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Patient Name");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ReligiousTicket.class))
                  ? new SimpleStringProperty(
                      ((ReligiousTicket) param.getValue().getValue()).getPatientName())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Religion");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ReligiousTicket.class))
                  ? new SimpleStringProperty(
                      ((ReligiousTicket) param.getValue().getValue()).getReligiousType())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col3 = makeColumn("Religious Denomination");
      col3.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ReligiousTicket.class))
                  ? new SimpleStringProperty(
                      ((ReligiousTicket) param.getValue().getValue()).getReligiousDenomination())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col4 = makeColumn("Date");
      col4.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ReligiousTicket.class))
                  ? new SimpleStringProperty(
                      ((ReligiousTicket) param.getValue().getValue()).getDate())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col5 = makeColumn("Time");
      col5.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(ReligiousTicket.class))
                  ? new SimpleStringProperty(
                      ((ReligiousTicket) param.getValue().getValue()).getTime())
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2, col3, col4, col5));
    } else if (type == SECURITY) {
      JFXTreeTableColumn<ServiceTicket, String> col1 = makeColumn("Type");
      col1.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(SecurityTicket.class))
                  ? new SimpleStringProperty(
                      ((SecurityTicket) param.getValue().getValue()).getSecurityType())
                  : new SimpleStringProperty());
      JFXTreeTableColumn<ServiceTicket, String> col2 = makeColumn("Emergency");
      col2.setCellValueFactory(
          param ->
              (param.getValue().getValue().getClass().equals(SecurityTicket.class))
                  ? new SimpleStringProperty(
                      Boolean.toString(
                          ((SecurityTicket) param.getValue().getValue()).isEmergency()))
                  : new SimpleStringProperty());
      columnList.addAll(Arrays.asList(col1, col2));
    }
    // These have no unique attributes
    //    else if (type == SANITATION) {
    //    } else if (type == MAINTENANCE) {
    //    }
    return columnList;
  }

  private static JFXTreeTableColumn<ServiceTicket, String> makeColumn(String name) {

    JFXTreeTableColumn<ServiceTicket, String> column = new JFXTreeTableColumn<>(name);
    column.setPrefWidth(150);
    column.setEditable(true);
    return column;
  }
}
