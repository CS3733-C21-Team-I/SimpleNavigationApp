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

  public Map<String, HospitalMap> loadMapsFromMemory() {

    Map<String, HospitalMap> results = new HashMap<>();

    Map<String, HospitalMapNode> tempNodeLookup = new HashMap<>();

    try {
      Statement stmt = databaseRef.getConnection().createStatement();
      ResultSet mapResult = stmt.executeQuery("SELECT * FROM navMaps");
      while (mapResult.next()) {
        try {
          Set<HospitalMapNode> nodes = new HashSet<>();

          String mapId = mapResult.getString("MAP_ID");
          String mapName = mapResult.getString("map_Name");
          int floor = mapResult.getInt("floor_Number");
          String buildingName = mapResult.getString("building_Name");
          String image_path = mapResult.getString("image_path");

          try {
            stmt = databaseRef.getConnection().createStatement();
            ResultSet nodeResults =
                stmt.executeQuery("SELECT * FROM navNodes WHERE map_ID='" + mapId + "'");

            while (nodeResults.next()) {
              String nodeId = nodeResults.getString("node_ID");
              int xCoord = nodeResults.getInt("x_Coord");
              int yCoord = nodeResults.getInt("y_Coord");

              HospitalMapNode node;
              switch (nodeResults.getString("NODE_TYPE")) {
                case "POS":
                  node = new HospitalMapNode(nodeId, mapId, xCoord, yCoord, new ArrayList<>());
                  break;
                case "LOC":
                  String shortName = nodeResults.getString("SHORT_NAME");
                  String longName = nodeResults.getString("LONG_NAME");
                  String teamAssigned = nodeResults.getString("TEAM_ASSIGNED");
                  node =
                      new LocationNode(
                          nodeId,
                          mapId,
                          xCoord,
                          yCoord,
                          shortName,
                          longName,
                          teamAssigned,
                          new ArrayList<>());
                  break;
                default:
                  throw new IllegalStateException(
                      "Found a NODE_TYPE value not handled in loadMapsFromMemory: "
                          + nodeResults.getString("NODE_TYPE"));
              }

              tempNodeLookup.put(node.getID(), node);
              nodes.add(node);
            }
          } catch (SQLException e) {
            System.out.println("Log error queerying nodes database");
            return null;
          }

          results.put(
              mapId, new HospitalMap(mapId, mapName, buildingName, floor, image_path, nodes));

        } catch (SQLException e) {
          e.printStackTrace();
          System.out.println("Log navMap column names not correct");
          return null;
        }
      }
    } catch (SQLException e) {
      System.out.println("Log error queerying map database");
      return null;
    }

    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM NAVEDGES");

      while (resultSet.next()) {
        String fromNode = resultSet.getString("FROM_NODE");
        String toNode = resultSet.getString("TO_NODE");
        if (!tempNodeLookup.containsKey(fromNode))
          throw new IllegalStateException(
              "Database contained edge connection for unloaded node at: " + fromNode);
        if (!tempNodeLookup.containsKey(toNode))
          throw new IllegalStateException(
              "Database contained edge connection for unloaded node at: " + toNode);
        tempNodeLookup.get(fromNode).getConnections().add(tempNodeLookup.get(toNode));
        tempNodeLookup.get(toNode).getConnections().add(tempNodeLookup.get(fromNode));
      }
    } catch (SQLException e) {
      System.out.println("Error querying NAV_EDGES database");
      e.printStackTrace();
    }

    return results;
  }

  void dropTables() {
    try {

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Edges table.
        stmt.execute("DROP TABLE navEdges");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the nodeAttribute table.
        stmt.execute("DROP TABLE nodeAttribute");
      } catch (SQLException ex) {
        ex.printStackTrace();
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Nodes table.
        stmt.execute("DROP TABLE navNodes");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Maps table.
        stmt.execute("DROP TABLE navMaps");
      } catch (SQLException ex) {
        // No need to report an error.
        // The table simply did not exist.
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void createTables() {
    try {

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE navMaps(map_ID varchar(45) NOT NULL,"
                + " map_Name varchar(45), floor_Number integer, building_Name varchar(45),"
                + " image_Path varchar(45),PRIMARY KEY (map_ID)) ");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Map table");
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE navNodes(node_ID varchar(45) NOT NULL,"
                + " x_Coord integer NOT NULL, y_Coord integer NOT NULL, node_Type varchar(3),"
                + "long_Name varchar(45), short_Name varchar(45), map_ID varchar(45), team_Assigned varchar(45),"
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

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE nodeAttribute(attribute_ID integer NOT NULL GENERATED ALWAYS AS IDENTITY, "
                + "locationCategory varchar(45), nodeRestriction varchar(45), nodeAtt_ID varchar(45) NOT NULL,"
                + "PRIMARY KEY (attribute_ID), "
                + "FOREIGN KEY (nodeAtt_ID) references navNodes(node_ID))");
        //System.out.println("nodeAttribute table created");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Edges table");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void saveMapsIntoMemory(Collection<HospitalMap> hMaps) {
    class EdgePair implements Comparable<EdgePair> {
      String fromId;
      String toId;

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

    Set<EdgePair> edgePairSet = new TreeSet<>();

    for (HospitalMap hMap : hMaps) {
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

      for (HospitalMapNode node : hMap.getNodes()) {
        for (HospitalMapNode toNode : node.getConnections()) {
          edgePairSet.add(new EdgePair(node.getID(), toNode.getID()));
        }

        try {
          Statement statement = databaseRef.getConnection().createStatement();
          if (node instanceof LocationNode) {
            // TODO locationNode handling
            statement.executeUpdate(
                "INSERT INTO navNodes (MAP_ID, NODE_ID, X_COORD, Y_COORD, NODE_TYPE, SHORT_NAME, LONG_NAME, TEAM_ASSIGNED)"
                    + "VALUES ('"
                    + hMap.getId()
                    + "', '"
                    + node.getID()
                    + "', "
                    + node.getxCoord()
                    + ", "
                    + node.getyCoord()
                    + ", 'LOC', '"
                    + ((LocationNode) node).getShortName()
                    + "', '"
                    + ((LocationNode) node).getLongName()
                    + "', '"
                    + ((LocationNode) node).getTeamAssigned()
                    + "')");

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
          System.out.println("Error inserting node: " + node.getID() + " into database");
          e.printStackTrace();
        }
      }
    }

    for (EdgePair pair : edgePairSet) {
      try {
        Statement statement = databaseRef.getConnection().createStatement();
        statement.executeUpdate(
            "INSERT INTO navEdges (from_Node, to_Node) "
                + "VALUES ('"
                + pair.fromId
                + "', '"
                + pair.toId
                + "')");
      } catch (SQLException e) { // TODO catch e
        e.printStackTrace();
      }
    }
  }
}
