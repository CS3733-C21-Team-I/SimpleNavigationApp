package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.ServiceTicketDatabaseManager;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicket;
import java.util.List;

public class ServiceTableIntermediateController {

  public List<ServiceTicket> getUserTickets() {
    return null;
  }

  public void addAssignedID(int serviceTicketID, int newAssignedID) {
    ServiceTicketDatabaseManager.getInstance().addEmployee(serviceTicketID, newAssignedID);
  }

  public void removeAssignedID(int serviceTicketID, int newAssignedID) {
    ServiceTicketDatabaseManager.getInstance().removeEmployee(serviceTicketID, newAssignedID);
  }

  public void markCompleted(int serviceTicketID) {
    ServiceTicketDatabaseManager.getInstance().updateTicket(serviceTicketID);
  }
}
