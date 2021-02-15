package edu.wpi.ithorian.hospitalMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HospitalMapCSVBuilder {

//  // represents  a node in a list for the purpose of loading into CSV
//  public List<String> nodeToList() {
//    List<String> nodeElements = new ArrayList<>();
//    nodeElements.add(this.id);
//    nodeElements.add(String.valueOf(this.xcoord));
//    nodeElements.add(String.valueOf(this.ycoord));
//    nodeElements.add(String.valueOf(this.floor));
//    nodeElements.add(this.building);
//    nodeElements.add(this.nodeType);
//    nodeElements.add(this.longname);
//    nodeElements.add(this.shortname);
//    nodeElements.add("I");
//
//
//  }





//  public Set<List<String>> hospitalMapNodesForCSV(){
//
//    Set<List<String>> setOfNodes= new HashSet<List<String>>();
//
//    for (HospitalMapNode)
//      setOfNodes.add()
//  }
  /** @param path */
  public HospitalMap loadCSV(String path) {
    // TODO - implement HospitalMapCSVBuilder.loadCSV
    throw new UnsupportedOperationException();
  }
}

//  public static writeFile(String nodeCSVFilePath,List<List<String>> rows) throws IOException {
//
//
//    FileWriter fileWriter = new FileWriter("newNodes.csv");
//
//
//    for (List<String> singleRow : rows) {
//      fileWriter.append(String.join(",", singleRow));
//      fileWriter.append("\n");
//    }
//
