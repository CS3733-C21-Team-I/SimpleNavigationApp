package edu.wpi.cs3733.c21.teamI.PathPlanning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.c21.teamI.hospitalMap.*;
import edu.wpi.cs3733.c21.teamI.pathfinding.DepthFirstSearch;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathPlanningAlgorithm;
import java.util.*;
import org.junit.jupiter.api.Test;

public class PathTest {
  HospitalMap map =
      HospitalMapCSVBuilder.loadCSV(
              System.getProperty("user.dir")
                  + "\\src/main/resources/edu/wpi/cs3733/c21/teamI/hospitalMap/testmapNodes.csv",
              System.getProperty("user.dir")
                  + "\\src/main/resources/edu/wpi/cs3733/c21/teamI/hospitalMap/testmapEdges.csv")
          .get("pathTest");

  // ------------------A Star--------------------------

  // Straight path from nodes 1 -> 11
  @Test
  public void obviousPathTestAStar() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    PathFinder aStar = new PathFinder();
    List<HospitalMapNode> actualPath =
        aStar.findPath(map.getNode("N01"), map.getNode("N11"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N01"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N11"));

    assertEquals(expectedPathA, actualPath);
  }

  // Two options for optimal paths from nodes 1 -> 13
  @Test
  public void multipleOptimalPathTestAStar() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    PathFinder aStar = new PathFinder();
    List<HospitalMapNode> actualPath =
        aStar.findPath(map.getNode("N01"), map.getNode("N13"), scorer);

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
  public void handicapPathTestAStar() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<NodeRestrictions> avoid = new ArrayList<>();
    avoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    scorer.nodeTypesToAvoid = avoid;

    map.getNode("N09").setNodeRestrictions(avoid);

    PathFinder aStar = new PathFinder();
    List<HospitalMapNode> actualPath =
        aStar.findPath(map.getNode("N06"), map.getNode("N08"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N06"));
    expectedPathA.add(map.getNode("N05"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N08"));

    assertEquals(expectedPathA, actualPath);
  }

  // ------------------Depth First Search--------------------------

  // Straight path from nodes 1 -> 11
  @Test
  public void obviousPathTestDFS() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    PathPlanningAlgorithm dfs = new DepthFirstSearch();
    List<HospitalMapNode> actualPath = dfs.findPath(map.getNode("N01"), map.getNode("N11"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N01"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N11"));

    assertEquals(expectedPathA, actualPath);
  }

  // Two options for optimal paths from nodes 1 -> 13
  @Test
  public void multipleOptimalPathTestDFS() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();

    PathPlanningAlgorithm dfs = new DepthFirstSearch();
    List<HospitalMapNode> actualPath = dfs.findPath(map.getNode("N01"), map.getNode("N13"), scorer);

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
  public void handicapPathTestDFS() {

    EuclidianDistCalc scorer = new EuclidianDistCalc();
    List<NodeRestrictions> avoid = new ArrayList<>();
    avoid.add(NodeRestrictions.WHEELCHAIR_INACCESSIBLE);
    scorer.nodeTypesToAvoid = avoid;

    map.getNode("N09").setNodeRestrictions(avoid);

    PathPlanningAlgorithm dfs = new DepthFirstSearch();
    List<HospitalMapNode> actualPath = dfs.findPath(map.getNode("N06"), map.getNode("N08"), scorer);

    List<HospitalMapNode> expectedPathA = new ArrayList<>();
    expectedPathA.add(map.getNode("N06"));
    expectedPathA.add(map.getNode("N05"));
    expectedPathA.add(map.getNode("N04"));
    expectedPathA.add(map.getNode("N07"));
    expectedPathA.add(map.getNode("N08"));

    assertEquals(expectedPathA, actualPath);
  }
}
