package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.notification.Notification;
import edu.wpi.cs3733.c21.teamI.user.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby://localhost:1527/navDB";

  private static NotificationManager ourInstance;

  public static void init(boolean regen) {
    ourInstance = new NotificationManager(regen);
  }

  public static NotificationManager getInstance() {
    return ourInstance;
  }

  private NotificationManager(boolean regen) {
    super(DB_URL, regen);
  }

  @Override
  void createTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute(
          "create table notification(\n"
              + "notif_ID integer NOT NULL GENERATED ALWAYS AS IDENTITY,\n"
              + "userID integer NOT NULL,\n"
              + "details varchar(450),\n"
              + "timestamp varchar(50),\n"
              + "hasDisplayed boolean DEFAULT false,\n"
              + "PRIMARY KEY (notif_ID),\n"
              + "FOREIGN KEY (userID) REFERENCES HOSPITAL_USERS(user_ID))");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  void dropTables() {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute("DROP TABLE notification");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Notification updateNotification(int notifID) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      Notification nt = getNotificationForId(notifID);
      nt.setHasDisplayed(true);
      stmt.executeUpdate(
          "UPDATE notification SET hasDisplayed = '"
              + true
              + "' WHERE NOTIF_ID = "
              + String.valueOf(notifID));
      return nt;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public int addNotification(Notification nt) {
    try {
      String query =
          "INSERT INTO notification(userID, details, timestamp)\n"
              + "VALUES ("
              + nt.getUserID()
              + ", '"
              + nt.getDetails()
              + "', '"
              + nt.getTimestamp()
              + "')";
      PreparedStatement stmt =
          databaseRef.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      stmt.execute();

      ResultSet rs = stmt.getGeneratedKeys();
      rs.next();

      return rs.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to insert notification");
    }
  }

  public void removeNotification(int id) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      stmt.execute("DELETE FROM notification WHERE NOTIF_ID = " + String.valueOf(id));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Notification getNotificationForId(int id) {
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM notification WHERE NOTIF_ID = " + String.valueOf(id));
      if (rs.next()) {
        Notification notif =
            new Notification(
                rs.getInt("userID"), rs.getString("details"), rs.getString("timestamp"));
        return notif;
      } else return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Notification> getPendingNotifications(User user) {
    List<Notification> notifs = new ArrayList<>();
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet rs =
          stmt.executeQuery(
              "SELECT * FROM notification WHERE USERID = " + String.valueOf(user.getUserId()));
      Notification nt;
      while (rs.next()) {
        nt =
            new Notification(
                rs.getInt("userID"), rs.getString("details"), rs.getString("timestamp"));
        nt.setNotificationID(rs.getInt("notif_ID"));
        notifs.add(nt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifs;
  }
}
