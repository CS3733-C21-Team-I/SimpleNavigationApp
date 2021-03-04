package edu.wpi.cs3733.c21.teamI;

import edu.wpi.cs3733.c21.teamI.database.UserDatabaseManager;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.util.HashSet;

public class ApplicationDataController {

  private static ApplicationDataController ourInstance;

  public static void init() {
    HashSet<User.Role> baseUserRoles = new HashSet<>();
    baseUserRoles.add(User.Role.BASE);
    HashSet<User.Permission> baseUserPermissions = new HashSet<>();
    User.baseUser = new User(-1, "Unknown User", baseUserRoles, baseUserPermissions);
    ourInstance = new ApplicationDataController();
  }

  public static ApplicationDataController getInstance() {
    return ourInstance;
  }

  private User loggedInUser;

  private ApplicationDataController() {
    loggedInUser = User.baseUser;
  }

  public User getLoggedInUser() {
    return loggedInUser;
  }

  /**
   * Called when logging in a new User
   *
   * @param username Username
   * @param password Password
   * @return True if valid log in false if not
   */
  public boolean logInUser(String username, String password) {
    User user;
    if (password == null || password.isEmpty()) {
      user = UserDatabaseManager.getInstance().getUserForScreenname(username);
    } else {
      user = UserDatabaseManager.getInstance().getUserWithPassword(username, password);
    }



    if (user == null) return false;

    loggedInUser = user;
    for (User.Role role : user.userRoles) {
      System.out.println(role.toString());
    }
    return true;
  }

  public boolean isLoggedIn() {
    return loggedInUser != User.baseUser;
  }

  public void logOutUser() {
    loggedInUser = User.baseUser;
  }
}
