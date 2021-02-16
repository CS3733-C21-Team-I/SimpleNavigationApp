package edu.wpi.ithorian.hospitalMap.mapEditing;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.ithorian.hospitalMap.HospitalMap;
import edu.wpi.ithorian.hospitalMap.HospitalMapNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class NavEditTests {

//  @Test
//  public void addNodeTest() {
//    MapEditManager.init();
//    HospitalMap map = new HospitalMap();
//    map.generateElementFromData(
//        readFromFile("/edu/wpi/ithorian/hopitalMap/mapEditing/SampleMapNodes.csv"),
//        readFromFile("/edu/wpi/ithorian/hopitalMap/mapEditing/SampleMapEdges.csv"));
//
//    MapEditManager.getInstance().setActiveMap(map);
//    for (int i = 0; i < 100; i++) {
//      MapEditManager.getInstance().addNode(new HospitalMapNode("N100" + i, 0, 0, null));
//    }
//    for (int i = 0; i < 100; i++) {
//      assertNotNull(map.getNode("N100" + i));
//    }
//  }

  public static List<List<String>> readFromFile(String path) {
    List<List<String>> allElements = new ArrayList<>();
    List<String> elementData;
    try {

      System.out.println(NavEditTests.class.getResource(path));
      BufferedReader br =
          new BufferedReader(new InputStreamReader(NavEditTests.class.getResourceAsStream(path)));
      String line;
      boolean skip = true;
      while ((line = br.readLine()) != null) {
        if (skip) {
          skip = false;
        } else {
          String[] values = line.split(",");
          elementData = Arrays.asList(values);
          allElements.add(elementData);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return allElements;
  }
}
