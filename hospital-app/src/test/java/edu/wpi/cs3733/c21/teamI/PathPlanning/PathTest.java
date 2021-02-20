package edu.wpi.cs3733.c21.teamI.PathPlanning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import java.util.*;
import org.junit.jupiter.api.Test;

public class PathTest {

  // Straight path from nodes 1 -> 11
  @Test
  public void obviousPathTest() {
    HospitalMap map =
        HospitalMapCSVBuilder.loadCSV("testmapNodes.csv", "testmapEdges.csv").get("pathTest");
    System.out.println(map);

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<HospitalMapNode> actualPath =
        PathFinder.findPath(map.getNode("N01"), map.getNode("N11"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N01"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N11"));

    assertEquals(expectedPathA, actualPath);
  }

  // Two options for optimal paths from nodes 1 -> 13
  @Test
  public void multipleOptimalPathTest() {
    HospitalMap map =
        HospitalMapCSVBuilder.loadCSV("testmapNodes.csv", "testmapEdges.csv").get("pathTest");
    System.out.println(map);

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<HospitalMapNode> actualPath =
        PathFinder.findPath(map.getNode("N01"), map.getNode("N13"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N01"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N11"));
    expectedPathA.add(map.getNode("N12"));
    expectedPathA.add(map.getNode("N13"));

    List<HospitalMapNode> expectedPathB = new ArrayList<>();
    expectedPathB.add(map.getNode("N01"));
    expectedPathB.add(map.getNode("N04"));
    expectedPathB.add(map.getNode("N07"));
    expectedPathB.add(map.getNode("N08"));
    expectedPathB.add(map.getNode("N12"));
    expectedPathB.add(map.getNode("N13"));

    assertTrue(actualPath.equals(expectedPathA) || actualPath.equals(expectedPathB));
  }

  // path from 6-> 8 where 9 is a staircase, meaning person must take the long way around
  @Test
  public void handicapPathTest() {
    HospitalMap map =
        HospitalMapCSVBuilder.loadCSV("testmapNodes.csv", "testmapEdges.csv").get("pathTest");
    System.out.println(map);

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<String> avoid = new ArrayList<>();
    avoid.add("STAI");
    scorer.nodeTypesToAvoid = avoid;

    List<HospitalMapNode> actualPath =
        PathFinder.findPath(map.getNode("N06"), map.getNode("N08"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N06"));
    expectedPathA.add(map.getNode("N05"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N08"));

    assertEquals(expectedPathA, actualPath);
  }
}
