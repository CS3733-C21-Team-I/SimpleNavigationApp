package edu.wpi.ithorian.database;

import edu.wpi.ithorian.HospitalMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

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

  public HospitalMap loadMapFromMemory(String mapId) {
      ResultSet mapResult = null;
      try {
          Statement stmt = connection.createStatement();
          mapResult = stmt.executeQuery("SELECT * FROM navMaps WHERE map_ID='" + mapId + "'");
          if (!mapResult.next()) {
              // Log attempting to acess map not in database
              return null;
          }
      } catch (SQLException e) {
          // Log error queerying map database
          return null;
      }


      String mapName, buildingName, teamAssigned;
      int floor;

      try {
          mapName = mapResult.getString("map_Name");
          floor = mapResult.getInt("floor_Number");
          buildingName = mapResult.getString("building_Name");
          teamAssigned = mapResult.getString("team_Assigned");
      } catch (SQLException e) {
          // Log navMap column names not correct
          return null;
      }

      ResultSet nodeResults;
      try {
          Statement stmt = connection.createStatement();
          nodeResults = stmt.executeQuery("SELECT * FROM navNodes WHERE map_ID='" + mapId + "'");
      } catch (SQLException e) {
          // Log error queerying map database
          return null;
      }

      Set<HospitalMap.Node> nodeSet = new HashSet<>();

      try {
          while (nodeResults.next()) {
              String nodeId, nodeType, longname, shortname;
              int xCoord, yCoord;
              nodeId = nodeResults.getString("node_ID");
              nodeType = nodeResults.getString("node_Type");
              xCoord = nodeResults.getInt("x_Coord");
              yCoord = nodeResults.getInt("y_Coord");
              longname = nodeResults.getString("long_Name");
              shortname = nodeResults.getString("short_Name");

              //TODO edges

              nodeSet.add(new HospitalMap.Node(nodeId, buildingName, nodeType, longname, shortname, teamAssigned, xCoord, yCoord, floor, null));


          }
      } catch (SQLException e) {
          //More error handling
          return null;
      }


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
                stmt.execute("CREATE TABLE navMaps(map_ID varchar(45) NOT NULL," +
                        " map_Name varchar(45), floor_Number integer, building_Name varchar(45)," +
                        " teamAssigned varchar(1), PRIMARY KEY (map_ID))");
            } catch (SQLException e) {
                System.out.println("Error generating Map table");
            }

            try{
                stmt.execute("CREATE TABLE navNodes(node_ID varchar(45) NOT NULL," +
                        " x_Coord integer NOT NULL, y_Coord integer NOT NULL,is_Named boolean, node_Type varchar(4)," +
                        "long_Name varchar(45), short_Name varchar(45),map_ID integer, " +
                        "PRIMARY KEY(node_ID), FOREIGN KEY (map_ID) references navMaps(map_ID))");
            } catch (SQLException e){
                System.out.println("Error generating Nodes table");
            }

            try{
                stmt.execute("CREATE TABLE navEdges(edge_ID varchar(45) NOT NULL, " +
                        "from_node varchar(45), to_node varchar(45), PRIMARY KEY(edge_ID), " +
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
