package edu.wpi.cs3733.c21.teamI.pathfinding;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import org.junit.jupiter.api.Test;

class TextDirectionsTest {

  @Test
  public void testGetAngle() {
    /*
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "edu/wpi/cs3733/c21/teamI/pathfinding/DirectionsSampleNodes.csv",
            "edu/wpi/cs3733/c21/teamI/pathfinding/DirectionsSampleEdges.csv");

    HospitalMap map1 = maps.get("map1");

    for (HospitalMapNode m : map1.getNodes()) {
      System.out.println(m);
    }
     */
    TextDirections test = new TextDirections();
    EuclidianDistCalc helper = new EuclidianDistCalc();

    // HospitalMapNode junction = new HospitalMapNode( String id, String mapID, int xCoord, int
    // yCoord, List<HospitalMapNode> connections);
    HospitalMapNode start = new HospitalMapNode("junction", "map1", 0, 5, null);
    HospitalMapNode junction = new HospitalMapNode("junction", "map1", 10, 5, null);
    HospitalMapNode left = new HospitalMapNode("left", "map1", 10, 0, null);
    HospitalMapNode right = new HospitalMapNode("right", "map1", 10, 10, null);
    HospitalMapNode straight = new HospitalMapNode("straight", "map1", 15, 5, null);

    assertEquals(180, TextDirections.angleDegrees(helper, start, junction, straight), 0.01);
    assertEquals(90, TextDirections.angleDegrees(helper, start, junction, left), 0.01);
    assertEquals(-90, TextDirections.angleDegrees(helper, start, junction, right), 0.01);

    // now going back the other way:
    assertEquals(180, TextDirections.angleDegrees(helper, straight, junction, start), 0.01);
    assertEquals(180, TextDirections.angleDegrees(helper, left, junction, right), 0.01);
    assertEquals(-90, TextDirections.angleDegrees(helper, left, junction, start), 0.01);
    assertEquals(90, TextDirections.angleDegrees(helper, right, junction, start), 0.01);

    // now some gradual lefts and rights:
    HospitalMapNode slightleft = new HospitalMapNode("slightleft", "map1", 17, 1, null);
    HospitalMapNode slightright = new HospitalMapNode("slightright", "map1", 17, 9, null);

    assertEquals(150, TextDirections.angleDegrees(helper, start, junction, slightleft), 1);
    assertEquals(-150, TextDirections.angleDegrees(helper, start, junction, slightright), 1);
  }

  @Test
  public void testDescribeDirections() {
    EuclidianDistCalc helper = new EuclidianDistCalc();

    // HospitalMapNode junction = new HospitalMapNode( String id, String mapID, int xCoord, int
    // yCoord, List<HospitalMapNode> connections);
    LocationNode start =
        new LocationNode("junction", "map1", 0, 5, "Start", "Starting location", "", null);
    HospitalMapNode junction = new HospitalMapNode("junction", "map1", 10, 5, null);
    LocationNode junction2 =
        new LocationNode("junction", "map1", 10, 5, "Crossroads Cafe", "Crossroads Cafe", "", null);
    HospitalMapNode left = new HospitalMapNode("left", "map1", 10, 0, null);
    HospitalMapNode right = new HospitalMapNode("right", "map1", 10, 10, null);
    HospitalMapNode slightleft = new HospitalMapNode("slightleft", "map1", 17, 1, null);
    HospitalMapNode slightright = new HospitalMapNode("slightright", "map1", 17, 9, null);
    HospitalMapNode straight = new HospitalMapNode("straight", "map1", 15, 5, null);
    LocationNode destination =
        new LocationNode("destination", "map1", 30, 5, "End", "Hospital Destination", "", null);

    assertEquals("Take a left.", TextDirections.describeStep(helper, start, junction, left));
    assertEquals("Take a right.", TextDirections.describeStep(helper, start, junction, right));

    assertEquals(
        "Take a slight left at Crossroads Cafe.",
        TextDirections.describeStep(helper, start, junction2, slightleft));
    assertEquals(
        "Take a slight right at Crossroads Cafe.",
        TextDirections.describeStep(helper, start, junction2, slightright));

    assertEquals(
        "Continue straight.", TextDirections.describeStep(helper, start, junction, straight));
    assertEquals(
        "Continue straight at Crossroads Cafe.",
        TextDirections.describeStep(helper, start, junction2, straight));
  }
}
