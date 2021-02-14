package edu.wpi.ithorian;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadCSV {

  // HERE AS A TEMPORARY MEASURE
  public static List<List<String>> readFromFile(String path) {
    List<List<String>> allElements = new ArrayList<>();
    List<String> elementData;
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
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
