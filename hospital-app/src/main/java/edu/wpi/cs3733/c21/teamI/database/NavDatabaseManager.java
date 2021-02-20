package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class NavDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby:navDB";

  private static NavDatabaseManager ourInstance;

  public static void init(boolean regen) {
    ourInstance = new NavDatabaseManager(regen);
  }

  public static NavDatabaseManager getInstance() {
    return ourInstance;
  }

  private NavDatabaseManager(boolean regen) {
    super(DB_URL, regen);
  }

  public HospitalMap loadMapFromMemory(String mapId) {
    ResultSet mapResult = null;
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      mapResult = stmt.executeQuery("SELECT * FROM navMaps WHERE map_ID='" + mapId + "'");
      if (!mapResult.next()) {
        System.out.println("Log attempting to acess map not in database");
        return null;
      }
    } catch (SQLException e) {
      System.out.println("Log error queerying map database");
      return null;
    }

    String mapName, buildingName, teamAssigned, image_path;
    int floor;

    try {
      mapName = mapResult.getString("map_Name");
      floor = mapResult.getInt("floor_Number");
      buildingName = mapResult.getString("building_Name");
      teamAssigned = mapResult.getString("teamAssigned");
      image_path = mapResult.getString("image_path");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Log navMap column names not correct");
      return null;
    }

    ResultSet nodeResults;
    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      nodeResults = stmt.executeQuery("SELECT * FROM navNodes WHERE map_ID='" + mapId + "'");
    } catch (SQLException e) {
      System.out.println("Log error queerying map database");
      return null;
    }

    Map<String, HospitalMapNode> nodeMap = new HashMap<>();

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

        nodeMap.put(nodeId, new HospitalMapNode(nodeId, mapId, xCoord, yCoord, null));
      }
    } catch (SQLException e) {
      System.out.println("Error handling for pulling data from navNodes");
      return null;
    }

    try {

      Iterator nodeIterator = nodeMap.entrySet().iterator();

      while (nodeIterator.hasNext()) {
        Map.Entry nodeEntry = (Map.Entry) nodeIterator.next();
        ArrayList<HospitalMapNode> connected = new ArrayList<>();
        Statement stmt = databaseRef.getConnection().createStatement();
        ResultSet fromEdgeResults =
            stmt.executeQuery(
                "SELECT * FROM navEdges WHERE from_Node='" + nodeEntry.getKey() + "'");
        while (fromEdgeResults.next()) {
          HospitalMapNode n = nodeMap.get(fromEdgeResults.getString("to_Node"));
          if (n == null) {
            System.out.println("Warning edge connection to nonexistant node");
            return null;
          }
          connected.add(n);
        }
        stmt = databaseRef.getConnection().createStatement();
        ResultSet toEdgeResults =
            stmt.executeQuery("SELECT * FROM navEdges WHERE to_Node='" + nodeEntry.getKey() + "'");
        while (toEdgeResults.next()) {
          HospitalMapNode n = nodeMap.get(toEdgeResults.getString("from_Node"));
          if (n == null) {
            System.out.println("Warning edge connection to nonexistant node");
            return null;
          }
          connected.add(n);
        }
        ((HospitalMapNode) nodeEntry.getValue()).setConnections(connected);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Log error querying edge database");
      return null;
    }

    return new HospitalMap(
        mapId, mapName, buildingName, floor, image_path, new HashSet<>(nodeMap.values()));
  }

  protected void dropTables() {
    try {

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Edges table.
        stmt.execute("DROP TABLE navEdges ");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Nodes table.
        stmt.execute("DROP TABLE navNodes ");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Maps table.
        stmt.execute("DROP TABLE navMaps ");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void createTables() {
    try {

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE navMaps(map_ID varchar(45) NOT NULL,"
                + " map_Name varchar(45), floor_Number integer, building_Name varchar(45),"
                + " team_Assigned varchar(1), image_Path varchar(45),PRIMARY KEY (map_ID)) ");
      } catch (SQLException e) {
        System.out.println("Error generating Map table");
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE navNodes(node_ID varchar(45) NOT NULL,"
                + " x_Coord integer NOT NULL, y_Coord integer NOT NULL, node_Type varchar(3),"
                + "long_Name varchar(45), short_Name varchar(45),map_ID varchar(45), "
                + "PRIMARY KEY(node_ID), FOREIGN KEY (map_ID) references navMaps(map_ID))");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Nodes table");
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE navEdges(edge_ID integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                + "from_Node varchar(45), to_Node varchar(45), PRIMARY KEY(edge_ID), "
                + "FOREIGN KEY (from_Node) references navNodes(node_ID),"
                + "FOREIGN KEY (to_Node) references navNodes(node_ID))");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Edges table");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void saveMapIntoMemory(HospitalMap hMap) {
    class EdgePair implements Comparable<EdgePair> {
      final String fromId;
      final String toId;

      public EdgePair(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
      }

      public int compareTo(EdgePair other) {
        boolean out =
            (this.fromId.equals(other.fromId) && this.toId.equals(other.toId))
                || (this.fromId.equals(other.toId) && this.toId.equals(other.fromId));
        if (out) {
          return 0;
        } else if (this.hashCode() > other.hashCode()) {
          return 1;
        } else {
          return -1;
        }
      }
    }

    try {
      Statement statement = databaseRef.getConnection().createStatement();
      statement.executeUpdate(
          "INSERT INTO navMaps (map_ID, map_Name, floor_Number, building_Name, image_Path) "
              + "VALUES ('"
              + hMap.getId()
              + "', '"
              + hMap.getMapName()
              + "', "
              + hMap.getFloorNumber()
              + ", '"
              + hMap.getBuildingName()
              + "', '"
              + hMap.getImagePath()
              + "')");
    } catch (SQLException e) {
      // TODO handle e
      e.printStackTrace();
    }

    Set<EdgePair> edgePairSet = new TreeSet<>();
    for (HospitalMapNode node : hMap.getNodes()) {
      for (HospitalMapNode toNode : node.getConnections()) {
        edgePairSet.add(new EdgePair(node.getID(), toNode.getID()));
      }

      try {
        Statement statement = databaseRef.getConnection().createStatement();
        if (node instanceof LocationNode) {
          // TODO locationNode handling
        } else {
          statement.executeUpdate(
              "INSERT INTO navNodes (node_ID, x_Coord, y_Coord, node_Type) "
                  + "VALUES ('"
                  + node.getID()
                  + "', "
                  + node.getxCoord()
                  + ", "
                  + node.getyCoord()
                  + ", "
                  + "'POS')");
        }

      } catch (SQLException e) {
        // TODO catch e
      }
    }

    for (EdgePair pair : edgePairSet) {
      System.out.println(pair.fromId + "," + pair.toId);
      try {
        Statement statement = databaseRef.getConnection().createStatement();
        statement.executeUpdate(
            "INSERT INTO navEdges (from_Node, to_Node) "
                + "VALUES ('"
                + pair.fromId
                + "', '"
                + pair.toId
                + "')");
      } catch (SQLException e) {
        // TODO catch e
        e.printStackTrace();
      }
    }
  }
}
