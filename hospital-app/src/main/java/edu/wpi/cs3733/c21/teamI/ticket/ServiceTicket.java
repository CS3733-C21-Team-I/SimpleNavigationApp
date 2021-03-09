package edu.wpi.cs3733.c21.teamI.ticket;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class ServiceTicket extends RecursiveTreeObject<ServiceTicket> {

  private int ticketID;
  private int requestingUserID;
  private ListProperty<Integer> assignedUserID;
  private TicketType ticketType;
  private String location;
  private String description;
  private BooleanProperty completed;
  // private String requestType;
  // private boolean emergency;

  public ServiceTicket() {}

  public ServiceTicket(
      int requestID, TicketType ticketType, String location, String desc, boolean completed) {
    this.requestingUserID = requestID;
    // this.requestType = requestType;
    this.ticketType = ticketType;
    this.location = location;
    this.description = desc;
    // this.emergency = emergency;
    this.completed = new SimpleBooleanProperty();
    this.completed.set(completed);
    assignedUserID = new SimpleListProperty<>(FXCollections.observableArrayList());
  }

  public enum TicketType {
    AUDIO_VISUAL,
    COMPUTER,
    LANGUAGE,
    PARKING,
    FLORAL,
    INTERNAL_TRANSPORTATION,
    EXTERNAL_TRANSPORTATION,
    LAUNDRY,
    MEDICINE,
    RELIGIOUS,
    SECURITY,
    SANITATION,
    MAINTENANCE,
    COVID
  }

  @Override
  public String toString() {
    return " FR0M: "
        + requestingUserID
        + "\tTO: "
        + assignedUserID
        + "\nTYPE: "
        + ticketType
        + "\nAT: "
        + location
        + "\nDESCPRIPTION: "
        + description
        + "\tCOMPLETED: "
        + completed;
  }

  public int getRequestingUserID() {
    return requestingUserID;
  }

  public int getTicketId() {
    return ticketID;
  }

  public List<Integer> getAssignedUserID() {
    return assignedUserID;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public String getLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed.getValue();
  }

  public BooleanProperty getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean bool) {
    this.completed.set(bool);
  }

  public void setTicketID(int id) {
    this.ticketID = id;
  }

  public void setAssignedUserID(List<Integer> employee) {
    this.assignedUserID.addAll(employee);
  }

  public void addAssignedUserID(int employee) {
    assignedUserID.add(new Integer(employee));
  }

  public ListProperty<Integer> assignedUserIDProperty() {
    return assignedUserID;
  }

  //  public String getRequestType() {
  //    return requestType;
  //  }
  //  public boolean isEmergency() {
  //    return emergency;
  //  }
}
