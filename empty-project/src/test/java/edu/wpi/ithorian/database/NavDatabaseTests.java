package edu.wpi.ithorian.database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class NavDatabaseTests {

  @Test
  public void saveMapTest() {
    NavDatabaseManager.init(true);

    NavDatabaseManager.getInstance().saveMapIntoMemory(getTestData());

    try {
      Statement statement =
          NavDatabaseManager.getInstance().databaseRef.getConnection().createStatement();

      ResultSet mapResults =
          statement.executeQuery("SELECT * FROM NAVMAPS WHERE MAP_ID='TestMap1'");
      assertTrue(mapResults.next());

      assertEquals("Test Map", mapResults.getString("map_Name"));
      assertEquals("test building", mapResults.getString("building_Name"));
      assertEquals(1, mapResults.getInt("floor_Number"));
      assertEquals("testPath", mapResults.getString("image_Path"));
    } catch (SQLException e) {

    }

    try {
      Statement statement =
          NavDatabaseManager.getInstance().databaseRef.getConnection().createStatement();

      ResultSet nodeResults =
          statement.executeQuery("SELECT * FROM NAVMAPS WHERE MAP_ID='TestMap1'");
      for (int i = 1; i <= 3; i++) {
        assertTrue(nodeResults.next());

        assertEquals("TestNode" + i, nodeResults.getString("node_Id"));
        assertEquals(-10 * i, nodeResults.getString("y_coord"));
        assertEquals(10 * i, nodeResults.getInt("x_Coord"));
        assertEquals("POS", nodeResults.getString("node_Type"));
      }
    } catch (SQLException e) {

    }

    try {
      Statement statement =
          NavDatabaseManager.getInstance().databaseRef.getConnection().createStatement();

      ResultSet edgeResults = statement.executeQuery("SELECT * FROM NAVEDGES");
      for (int i = 0; i < 2; i++) {
        assertTrue(edgeResults.next());
      }
    } catch (SQLException e) {

    }
  }

  private HospitalMap getTestData() {
    Set<HospitalMapNode> testNodes = new HashSet<>();

    HospitalMapNode n1 = new HospitalMapNode("TestNode1", "", 10, -10, new ArrayList<>());
    HospitalMapNode n2 = new HospitalMapNode("TestNode2", "", 20, -20, new ArrayList<>());
    HospitalMapNode n3 = new HospitalMapNode("TestNode3", "", 30, -30, new ArrayList<>());

    n1.getConnections().add(n2);
    n1.getConnections().add(n3);

    n2.getConnections().add(n1);
    n3.getConnections().add(n1);

    testNodes.add(n1);
    testNodes.add(n2);
    testNodes.add(n3);

    HospitalMap testMap =
        new HospitalMap("TestMap1", "Test Map", "test building", 1, "testPath", testNodes);

    return testMap;
  }
}
