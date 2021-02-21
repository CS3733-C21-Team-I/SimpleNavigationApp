package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class NavEditTests {

  @Test
  public void mapCloneConstructorTest() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    HospitalMap map1 = maps.get("map1");
    HospitalMap newMap1 = new HospitalMap(maps.get("map1"));

    assertEquals(newMap1.getId(), map1.getId());
    assertEquals(newMap1.getMapName(), map1.getMapName());
    assertEquals(newMap1.getBuildingName(), map1.getBuildingName());
    assertEquals(newMap1.getImagePath(), map1.getImagePath());
    assertEquals(newMap1.getFloorNumber(), map1.getFloorNumber());

    assertEquals(newMap1.getNodes().size(), map1.getNodes().size());

    newMap1.getNodes().add(new HospitalMapNode("N14", "map1", 0, 0, new ArrayList<>()));

    assertEquals(newMap1.getNodes().size(), map1.getNodes().size() + 1);

    assertTrue(newMap1.getNode("N01").getConnections().contains(newMap1.getNode("N04")));

    assertTrue(newMap1.getNode("N07").getConnections().contains(maps.get("map2").getNode("N11")));
  }

  @Test
  public void testAddNode() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.addNode(new HospitalMapNode("N100", "map1", 0, 0, new ArrayList<>()));

    assertNotNull(controller.getActiveMap().getNode("N100"));
  }

  @Test
  public void testEditNode() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.editNode("N04", new HospitalMapNode("", "map1", 100, -100, new ArrayList<>()));

    assertEquals(100, controller.getActiveMap().getNode("N04").getxCoord());
    assertEquals(-100, controller.getActiveMap().getNode("N04").getyCoord());

    controller.editNode(
        "N05",
        new LocationNode(
            "", "map1", 50, -50, "testEdit", "longerTestEdit", "I", new ArrayList<>()));

    assertEquals("testEdit", ((LocationNode) maps.get("map1").getNode("N05")).getShortName());
  }

  @Test
  public void testRemoveNode() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.deleteNode("N04");

    System.out.println(controller.getActiveMap());
  }

  @Test
  public void testAddEdge() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");
    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.addEdge("N01", "N06");

    assertTrue(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N06")));
    assertTrue(
        controller
            .getActiveMap()
            .getNode("N06")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));
  }

  @Test
  public void testRemoveEdge() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");
    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.deleteEdge("N01", "N04");

    assertFalse(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N04")));
    assertFalse(
        controller
            .getActiveMap()
            .getNode("N04")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));
  }

  @Test
  public void testUndoRedoAdd() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.addNode(new HospitalMapNode("N100", "map1", 0, 0, new ArrayList<>()));

    assertNotNull(controller.getActiveMap().getNode("N100"));

    controller.undo();

    try {
      assertNull(controller.getActiveMap().getNode("N100"));
    } catch (Exception e) {
    }

    controller.redo();

    assertNotNull(controller.getActiveMap().getNode("N100"));
  }

  @Test
  public void testUndoRedoEdit() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.editNode("N04", new HospitalMapNode("", "map1", 100, -100, new ArrayList<>()));

    assertEquals(100, controller.getActiveMap().getNode("N04").getxCoord());
    assertEquals(-100, controller.getActiveMap().getNode("N04").getyCoord());

    controller.undo();

    assertEquals(30, controller.getActiveMap().getNode("N04").getxCoord());

    controller.redo();

    assertEquals(100, controller.getActiveMap().getNode("N04").getxCoord());
    assertEquals(-100, controller.getActiveMap().getNode("N04").getyCoord());
  }

  @Test
  public void deleteNodeUndoRedo() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.deleteNode("N04");

    try {
      assertNull(controller.getActiveMap().getNode("N04"));
    } catch (Exception e) {
    }

    controller.undo();

    assertNotNull(controller.getActiveMap().getNode("N04"));

    controller.redo();

    try {
      assertNull(controller.getActiveMap().getNode("N04"));
    } catch (Exception e) {
    }
  }

  @Test
  public void testUndoRedoAddEdge() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");
    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.addEdge("N01", "N06");

    assertTrue(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N06")));
    assertTrue(
        controller
            .getActiveMap()
            .getNode("N06")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));

    controller.undo();

    assertFalse(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N06")));
    assertFalse(
        controller
            .getActiveMap()
            .getNode("N06")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));

    controller.redo();

    assertTrue(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N06")));
    assertTrue(
        controller
            .getActiveMap()
            .getNode("N06")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));
  }

  @Test
  public void testUndoRedoDeleteEdge() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");
    MapEditDataController controller = new MapEditDataController();
    controller.setActiveMap(maps.get("map1"));

    controller.deleteEdge("N01", "N04");

    assertFalse(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N04")));
    assertFalse(
        controller
            .getActiveMap()
            .getNode("N04")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));

    controller.undo();

    assertTrue(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N04")));
    assertTrue(
        controller
            .getActiveMap()
            .getNode("N04")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));

    controller.redo();

    assertFalse(
        controller
            .getActiveMap()
            .getNode("N01")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N04")));
    assertFalse(
        controller
            .getActiveMap()
            .getNode("N04")
            .getConnections()
            .contains(controller.getActiveMap().getNode("N01")));
  }
}
