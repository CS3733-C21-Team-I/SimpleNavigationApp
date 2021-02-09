package edu.wpi.ithorian.database;

import edu.wpi.ithorian.HospitalMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NavDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";

  private static NavDatabaseManager ourInstance;
  private Connection connection;

  public static void init(boolean regen) {
    ourInstance = new NavDatabaseManager(regen);
  }

  private NavDatabaseManager(boolean regen) {
    super(DB_URL, regen);
  }

  public HospitalMap loadMapFromMemory(int id) {

      return null;
  }
  
  void dropTables() {
        try{
            Statement stmt = connection.createStatement();
            try {
                // Drop the Edges table.
                stmt.execute("DROP TABLE navEdges ");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }

            try {
                // Drop the Nodes table.
                stmt.execute("DROP TABLE navNodes ");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }

            try {
                // Drop the Maps table.
                stmt.execute("DROP TABLE navMaps ");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

  void createTables() {
        try {
            Statement stmt = connection.createStatement();
            try {
                stmt.execute("CREATE TABLE navMaps(map_ID integer NOT NULL," +
                        " map_Name varchar(45), floor_Number integer, building_Name varchar(45)," +
                        " teamAssigned varchar(1), PRIMARY KEY (map_ID))");
            } catch (SQLException e) {
                System.out.println("Error generating Map table");
            }

            try{
                stmt.execute("CREATE TABLE navNodes(node_ID integer NOT NULL," +
                        " x_Coord integer NOT NULL, y_Coord integer NOT NULL,is_Named boolean, node_Type varchar(4)," +
                        "long_Name varchar(45), short_Name varchar(45),map_ID integer, " +
                        "PRIMARY KEY(node_ID), FOREIGN KEY (map_ID) references navMaps(map_ID))");
            } catch (SQLException e){
                System.out.println("Error generating Nodes table");
            }

            try{
                stmt.execute("CREATE TABLE navEdges(edge_ID integer NOT NULL, " +
                        "from_node integer, to_node integer, PRIMARY KEY(edge_ID), " +
                        "FOREIGN KEY (from_node) references navNodes(node_ID)," +
                        "FOREIGN KEY (to_node) references navNodes(node_ID))");
            } catch (SQLException e){
                System.out.println("Error generating Edges table");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
