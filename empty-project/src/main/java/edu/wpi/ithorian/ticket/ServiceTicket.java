package edu.wpi.ithorian.ticket;

import edu.wpi.ithorian.hospitalMap.LocationNode;

public class ServiceTicket {

	private int ticketId;
	private int requestingUserID;
	private int assignedUserID;
	private TicketType ticketType;
	private LocationNode location;
	private String description;
	private boolean completed;

	public void closeTicket() {
		// TODO - implement ServiceTicket.closeTicket
		throw new UnsupportedOperationException();
	}

	public void updateTicket() {
		// TODO - implement ServiceTicket.updateTicket
		throw new UnsupportedOperationException();
	}

	public void openTicket() {
		// TODO - implement ServiceTicket.openTicket
		throw new UnsupportedOperationException();
	}


	public enum TicketType {
		LAUNDRY,
		FOOD,
		SECURITY,
		MAINTENANCE
	}

}