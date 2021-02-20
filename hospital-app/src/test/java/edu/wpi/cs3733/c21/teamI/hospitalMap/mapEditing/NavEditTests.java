package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
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

    System.out.println(maps.get("map1"));
    System.out.println(maps.get("map2"));

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
}
