package edu.wpi.ithorian.database;

import edu.wpi.ithorian.user.User;
import java.util.List;

public class UserDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";
  private static UserDatabaseManager ourInstance;

  /**
   * Singleton init
   *
   * @param regen Set true if database should be wiped and reloaded from sources
   */
  public static void init(boolean regen) {
    ourInstance = new UserDatabaseManager(regen);
    // TODO reload from CSV
  }

  /**
   * Singleton access
   *
   * @return the singleton
   */
  public static UserDatabaseManager getInstance() {
    return ourInstance;
  }

  /**
   * Constructor for UserDatabase
   *
   * @param regen
   */
  private UserDatabaseManager(boolean regen) {
    super(DB_URL, regen);
    // TODO probably more stuff
  }

  /** @param userId */
  public User getUserForId(String userId) {
    // TODO - implement UserDatabaseManager.getUserForId
    throw new UnsupportedOperationException();
  }

  public String getDisplayNameForId() {
    // TODO - implement UserDatabaseManager.getDisplayNameForId
    throw new UnsupportedOperationException();
  }

  /** @param roleId */
  public List<String> getUsersWithRole(int roleId) {
    // TODO - implement UserDatabaseManager.getUsersWithRole
    throw new UnsupportedOperationException();
  }

  @Override
  protected void createTables() {}

  @Override
  protected void dropTables() {}
}
