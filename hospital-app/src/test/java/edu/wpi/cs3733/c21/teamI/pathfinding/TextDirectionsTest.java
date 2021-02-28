package edu.wpi.cs3733.c21.teamI.pathfinding;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TextDirectionsTest {

  @Test
  public void testGetAngle() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV(
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapNodes.csv",
            "/edu/wpi/cs3733/c21/teamI/hopitalMap/SampleMapEdges.csv");

    HospitalMap map1 = maps.get("map1");

    for (HospitalMapNode m : map1.getNodes()) {
      System.out.println(m);
    }
  }
}
