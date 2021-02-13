package edu.wpi.ithorian.database;

import edu.wpi.ithorian.user.User;
import java.util.List;

public class UserDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "";
  private UserDatabaseManager ourInstance;

  /** @param regen */
  private UserDatabaseManager(boolean regen) {
    super(DB_URL, regen);
    // TODO - implement UserDatabaseManager.UserDatabaseManager
    throw new UnsupportedOperationException();
  }

  /** @param regen */
  public static void init(boolean regen) {
    // TODO - implement UserDatabaseManager.init
    throw new UnsupportedOperationException();
  }

  public static UserDatabaseManager getInstance() {
    // TODO - implement UserDatabaseManager.getInstance
    throw new UnsupportedOperationException();
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
