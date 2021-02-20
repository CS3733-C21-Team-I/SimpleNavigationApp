package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapCSVBuilder;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class NavEditTests {

  @Test
  public void mapCloneConstructorTest() {
    Map<String, HospitalMap> maps =
        HospitalMapCSVBuilder.loadCSV("SampleMapNodes.csv", "SampleMapEdges.csv");
  }
}
