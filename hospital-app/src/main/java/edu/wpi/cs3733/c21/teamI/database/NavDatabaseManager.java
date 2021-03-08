package edu.wpi.cs3733.c21.teamI.database;

import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing.NavEditOperation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class NavDatabaseManager extends DatabaseManager {

  private static final String DB_URL = "jdbc:derby://localhost:1527/navDB";

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
                  String locationCategory = nodeResults.getString("location_category");
                  node =
                      new LocationNode(
                          nodeId,
                          mapId,
                          xCoord,
                          yCoord,
                          shortName,
                          longName,
                          LocationCategory.valueOf(locationCategory),
                          teamAssigned,
                          new ArrayList<>());
                  break;
                default:
                  throw new IllegalStateException(
                      "Found a NODE_TYPE value not handled in loadMapsFromMemory: "
                          + nodeResults.getString("NODE_TYPE"));
              }

              Statement attStatement = databaseRef.getConnection().createStatement();
              ResultSet attributeResults =
                  attStatement.executeQuery(
                      "SELECT * FROM NODE_ATTRIBUTE JOIN NODE_TO_ATTRIBUTE NTA on NODE_ATTRIBUTE.ATTRIBUTE_ID = NTA.ATTRIBUTE_ID WHERE NTA.NODE_ID='"
                          + nodeId
                          + "'");

              while (attributeResults.next()) {
                // System.out.println(attributeResults.getString("NODERESTRICTION"));
                node.getNodeRestrictions()
                    .add(NodeRestrictions.valueOf(attributeResults.getString("NODERESTRICTION")));
              }
              // System.out.println(nodeId + " : " + mapId);

              tempNodeLookup.put(node.getID(), node);
              nodes.add(node);
            }
          } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Log error querying nodes database");
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
      System.out.println("Log error querying map database");
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
        stmt.execute("DROP TABLE NODE_TO_ATTRIBUTE");
      } catch (SQLException ex) {
        // ex.printStackTrace();
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Edges table.
        stmt.execute("DROP TABLE navEdges");
      } catch (SQLException ex) {
        // ex.printStackTrace();
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the nodeAttribute table.
        stmt.execute("DROP TABLE NODE_ATTRIBUTE");
      } catch (SQLException ex) {
        ex.printStackTrace();
        //        ex.printStackTrace();
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Nodes table.
        stmt.execute("DROP TABLE navNodes");
      } catch (SQLException ex) {
        // ex.printStackTrace();
        // No need to report an error.
        // The table simply did not exist.
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        // Drop the Maps table.
        stmt.execute("DROP TABLE navMaps");
      } catch (SQLException ex) {
        // ex.printStackTrace();
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
                + "long_Name varchar(45), short_Name varchar(45), location_category varchar(10), map_ID varchar(45), team_Assigned varchar(45),"
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
            "CREATE TABLE node_Attribute(attribute_ID integer NOT NULL GENERATED ALWAYS AS IDENTITY, "
                + "nodeRestriction varchar(45),"
                + "PRIMARY KEY (attribute_ID))");
        // System.out.println("nodeAttribute table created");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Arrtibute table");
      }

      try {
        Statement stmt = databaseRef.getConnection().createStatement();
        stmt.execute(
            "CREATE TABLE NODE_TO_ATTRIBUTE("
                + "attribute_ID integer NOT NULL,"
                + "node_ID varchar(45) NOT NULL,"
                + "PRIMARY KEY (attribute_ID, node_ID),"
                + "FOREIGN KEY (attribute_ID) REFERENCES NODE_ATTRIBUTE(attribute_ID),"
                + "FOREIGN KEY (node_ID) REFERENCES NAVNODES(node_ID))");
        // System.out.println("nodeAttribute table created");
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error generating Node_To_Attribute table");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void saveMapsIntoMemory(Collection<HospitalMap> hMaps) {
    class EdgePair {
      String fromId;
      String toId;

      public EdgePair(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePair edgePair = (EdgePair) o;
        return (this.fromId.equals(edgePair.fromId) && this.toId.equals(edgePair.toId))
            || (this.fromId.equals(edgePair.toId) && this.toId.equals(edgePair.fromId));
      }

      @Override
      public int hashCode() {
        return Objects.hash(fromId) + Objects.hashCode(toId);
      }
    }

    Set<EdgePair> edgePairSet = new HashSet<>();

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
                "INSERT INTO navNodes (MAP_ID, NODE_ID, X_COORD, Y_COORD, NODE_TYPE, SHORT_NAME, LONG_NAME, location_category, TEAM_ASSIGNED)"
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
                    + ((LocationNode) node).getLocationCategory().toString()
                    + "', '"
                    + ((LocationNode) node).getTeamAssigned()
                    + "')");

          } else {
            statement.executeUpdate(
                "INSERT INTO navNodes (MAP_ID, node_ID, x_Coord, y_Coord, node_Type) "
                    + "VALUES ('"
                    + hMap.getId()
                    + "', '"
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

  public void applyNavEditOperations(Queue<NavEditOperation> operations) {
    while (!operations.isEmpty()) {
      NavEditOperation op = operations.poll();
      System.out.println(op.getOpType());
      switch (op.getOpType()) {
        case ADD_NODE:
          try {
            Statement statement = databaseRef.getConnection().createStatement();
            HospitalMapNode node = op.getNewNode();
            if (node instanceof LocationNode) {
              // TODO locationNode handling
              statement.executeUpdate(
                  "INSERT INTO navNodes (MAP_ID, NODE_ID, X_COORD, Y_COORD, NODE_TYPE, SHORT_NAME, LONG_NAME, TEAM_ASSIGNED)"
                      + "VALUES ('"
                      + node.getMapID()
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
                  "INSERT INTO navNodes (MAP_ID, node_ID, x_Coord, y_Coord, node_Type) "
                      + "VALUES ('"
                      + node.getMapID()
                      + "', '"
                      + node.getID()
                      + "', "
                      + node.getxCoord()
                      + ", "
                      + node.getyCoord()
                      + ", "
                      + "'POS')");
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case EDIT_NODE:
          try {
            Statement statement = databaseRef.getConnection().createStatement();
            HospitalMapNode node = op.getNewNode();
            if (node instanceof LocationNode) {
              // TODO locationNode handling
              statement.executeUpdate(
                  "UPDATE navNodes SET "
                      + "MAP_ID = '"
                      + node.getMapID()
                      + "', X_COORD = "
                      + node.getxCoord()
                      + ", Y_COORD = "
                      + node.getyCoord()
                      + ", NODE_TYPE = 'LOC', SHORT_NAME = '"
                      + ((LocationNode) node).getShortName()
                      + "', LONG_NAME = '"
                      + ((LocationNode) node).getLongName()
                      + "', TEAM_ASSIGNED = '"
                      + ((LocationNode) node).getTeamAssigned()
                      + "' WHERE NODE_ID = '"
                      + op.getTargetNode()
                      + "'");

            } else {
              statement.executeUpdate(
                  "UPDATE navNodes SET "
                      + "MAP_ID='"
                      + node.getMapID()
                      + "', X_COORD="
                      + node.getxCoord()
                      + ", Y_COORD="
                      + node.getyCoord()
                      + ", NODE_TYPE="
                      + "'POS' "
                      + "WHERE NODE_ID='"
                      + op.getTargetNode()
                      + "'");
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case DELETE_NODE:
          try {
            Statement statement = databaseRef.getConnection().createStatement();
            statement.executeUpdate(
                "DELETE FROM NAVEDGES WHERE TO_NODE='"
                    + op.getTargetNode()
                    + "' OR FROM_NODE='"
                    + op.getTargetNode()
                    + "'");
          } catch (SQLException e) {
            e.printStackTrace();
          }

          try {
            Statement statement = databaseRef.getConnection().createStatement();
            statement.executeUpdate(
                "DELETE FROM NAVNODES WHERE NODE_ID='" + op.getTargetNode() + "'");
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case DELETE_EDGE:
          try {
            Statement statement = databaseRef.getConnection().createStatement();
            statement.executeUpdate(
                "DELETE FROM NAVEDGES WHERE (TO_NODE='"
                    + op.getTargetNode()
                    + "' AND FROM_NODE='"
                    + op.getToNode()
                    + "') OR (TO_NODE='"
                    + op.getToNode()
                    + "' AND FROM_NODE='"
                    + op.getTargetNode()
                    + "')");
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case ADD_EDGE:
          try {
            Statement statement = databaseRef.getConnection().createStatement();
            statement.executeUpdate(
                "INSERT INTO NAVEDGES(FROM_NODE, TO_NODE) VALUES ('"
                    + op.getTargetNode()
                    + "', '"
                    + op.getToNode()
                    + "')");
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        default:
          throw new IllegalStateException(
              "Attempted to apply a unsuported NavEditOperation to database");
      }
    }
  }

  public String getMapIdFromLongName(String longName) {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet rs =
          statement.executeQuery("SELECT * FROM NAVNODES WHERE LONG_NAME='" + longName + "'");

      if (!rs.next()) return "ERRROR";

      return rs.getString("NODE_ID");
    } catch (SQLException e) {
      e.printStackTrace();
      return "ERROR";
    }
  }

  public static void populateExampleData() {
    try {
      Statement stmt =
          NavDatabaseManager.getInstance().databaseRef.getConnection().createStatement();
      stmt.addBatch(
          "INSERT INTO NODE_ATTRIBUTE (NODERESTRICTION) VALUES\n"
              + "                                                    ('PPE_REQUIRED'),\n"
              + "                                                    ('STAFF_ONLY'),\n"
              + "                                                    ('WHEELCHAIR_INACCESSIBLE')");

      stmt.addBatch(
          "INSERT INTO NODE_TO_ATTRIBUTE (NODE_ID, ATTRIBUTE_ID) VALUES\n"
              + "('ISTAI00101', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00102', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00103', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00104', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00105', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00201', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00202', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00203', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00204', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00205', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00301', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00304', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00305', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00401', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00404', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00405', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00504', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE')),"
              + "('ISTAI00505', (SELECT ATTRIBUTE_ID FROM NODE_ATTRIBUTE WHERE NODERESTRICTION='WHEELCHAIR_INACCESSIBLE'))");
      stmt.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<String> getLocationNodeLongNames() {
    try {
      Statement statement = databaseRef.getConnection().createStatement();
      ResultSet resultSet =
          statement.executeQuery("SELECT long_name FROM NAVNODES WHERE node_type='LOC'");
      List<String> out = new ArrayList<>();
      while (resultSet.next()) {
        out.add(resultSet.getString("long_name"));
      }
      return out;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
