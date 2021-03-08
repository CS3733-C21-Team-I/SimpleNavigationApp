package edu.wpi.cs3733.c21.teamI.ticket;

import static edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket.TicketType.*;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class UniqueColumnFactory {
  private static JFXTreeTableView<ServiceTicket> table;

  public static ArrayList<JFXTreeTableColumn<ServiceTicket, String>> getColumns(
      JFXTreeTableView<ServiceTicket> treeView, ServiceTicket ticket) {
    table = treeView;
    ArrayList<JFXTreeTableColumn<ServiceTicket, String>> columnList = new ArrayList();
    ServiceTicket.TicketType type = ticket.getTicketType();
    if (type == AUDIO_VISUAL) {
      columnList.add(makeColumn("Patient Name", ((AudioVisualTicket) ticket).getPatientName()));
      columnList.add(makeColumn("Media Type", ((AudioVisualTicket) ticket).getMediaType()));
    } else if (type == COMPUTER) {
      columnList.add(makeColumn("Computer Type", ((ComputerTicket) ticket).getComputerType()));
      columnList.add(
          makeColumn("Urgency", Boolean.toString(((ComputerTicket) ticket).isUrgency())));
    } else if (type == LANGUAGE) {
      columnList.add(makeColumn("Language", ((LanguageTicket) ticket).getLanguage()));
      columnList.add(makeColumn("Meeting Time", ((LanguageTicket) ticket).getMeetingTime()));
      columnList.add(
          makeColumn("Involves Legal", Boolean.toString(((LanguageTicket) ticket).isLegalDocs())));
    } else if (type == FLORAL) {
      columnList.add(makeColumn("Patient Name", ((FloralTicket) ticket).getPatientName()));
      columnList.add(makeColumn("Delivery Date", ((FloralTicket) ticket).getDeliveryDate()));
      columnList.add(makeColumn("Delivery Time", ((FloralTicket) ticket).getDeliveryTime()));
      //    } else if (type == GIFT) {
      //      columnList.add(makeColumn("Patient Name", ((GiftTicket) ticket).getPatientName()));
      //      columnList.add(makeColumn("Delivery Date", ((GiftTicket) ticket).getDeliveryDate()));
      //      columnList.add(makeColumn("Delivery Time", ((GiftTicket) ticket).getDeliveryTime()));
      //      columnList.add(makeColumn("Gift Type", ((GiftTicket) ticket).getGiftType()));
    } else if (type == INTERNAL_TRANSPORTATION) {
      columnList.add(
          makeColumn("Pick Up Date", ((InternalTransportationTicket) ticket).getPickUpDate()));
      columnList.add(
          makeColumn("Pick Up Time", ((InternalTransportationTicket) ticket).getPickUpTime()));
      columnList.add(
          makeColumn(
              "Emergency",
              Boolean.toString(((InternalTransportationTicket) ticket).isEmergency())));
      columnList.add(
          makeColumn(
              "Transportation Type",
              ((InternalTransportationTicket) ticket).isStretcher() ? "Stretcher" : "Wheelchair"));
    } else if (type == EXTERNAL_TRANSPORTATION) {
      columnList.add(
          makeColumn("Pick Up Date", ((ExternalTransportationTicket) ticket).getPickUpDate()));
      columnList.add(
          makeColumn("Pick Up Time", ((ExternalTransportationTicket) ticket).getPickUpTime()));
      columnList.add(
          makeColumn("Destination", ((ExternalTransportationTicket) ticket).getDestination()));
    } else if (type == LAUNDRY) {
      columnList.add(makeColumn("Pick Up Date", ((LaundryTicket) ticket).getPickUpDate()));
      columnList.add(makeColumn("Pick Up Time", ((LaundryTicket) ticket).getPickUpTime()));
      columnList.add(
          makeColumn("Dry Clean", Boolean.toString(((LaundryTicket) ticket).isDryClean())));
    } else if (type == MEDICINE) {
      columnList.add(makeColumn("Patient Name", ((MedicineTicket) ticket).getPatientName()));
      columnList.add(makeColumn("Drug Name", ((MedicineTicket) ticket).getDrugName()));
      columnList.add(makeColumn("Dose", ((MedicineTicket) ticket).getDose()));
      columnList.add(makeColumn("Date", ((MedicineTicket) ticket).getDate()));
      columnList.add(makeColumn("Time", ((MedicineTicket) ticket).getTime()));
    } else if (type == RELIGIOUS) {
      //      this.patientName = patientName;
      columnList.add(makeColumn("Patient Name", ((ReligiousTicket) ticket).getPatientName()));
      columnList.add(makeColumn("Religion", ((ReligiousTicket) ticket).getReligiousType()));
      columnList.add(
          makeColumn(
              "Religious Denomination", ((ReligiousTicket) ticket).getReligiousDenomination()));
      columnList.add(makeColumn("Date", ((ReligiousTicket) ticket).getDate()));
      columnList.add(makeColumn("Time", ((ReligiousTicket) ticket).getTime()));
    } else if (type == SECURITY) {
      columnList.add(makeColumn("Type", ((SecurityTicket) ticket).getSecurityType()));
      columnList.add(
          makeColumn("Emergency", Boolean.toString(((SecurityTicket) ticket).isEmergency())));
    }
    // These have no unique attributes
    //    else if (type == SANITATION) {
    //    } else if (type == MAINTENANCE) {
    //    }
    return columnList;
  }

  private static JFXTreeTableColumn makeColumn(String name, String value) {
    //    if(table.getColumns())
    JFXTreeTableColumn<ServiceTicket, String> column = new JFXTreeTableColumn<>(name);
    column.setCellValueFactory(param -> new SimpleStringProperty(value));
    column.setPrefWidth(150);
    column.setEditable(true);
    return column;
  }
}
