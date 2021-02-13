package edu.wpi.ithorian.database;

import edu.wpi.ithorian.database.DatabaseManager;

public class ServiceTicketDatabaseManager extends DatabaseManager {

	private static final String DB_URL = "";
	private ServiceTicketDatabaseManager ourInstance;

	/**
	 * 
	 * @param regen
	 */
	private ServiceTicketDatabaseManager(boolean regen) {
		super(DB_URL, regen);
		// TODO - implement ServiceTicket.ServiceTicketDatabaseManager
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param regen
	 */
	public void init(boolean regen) {
		// TODO - implement ServiceTicket.init
		throw new UnsupportedOperationException();
	}

	public ServiceTicketDatabaseManager getInstance() {
		// TODO - implement ServiceTicket.getInstance
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public ServiceTicketDatabaseManager getTicketForId(int id) {
		// TODO - implement ServiceTicket.getTicketForId
		throw new UnsupportedOperationException();
	}

	@Override
	protected void createTables() {

	}

	@Override
	protected void dropTables() {

	}
}