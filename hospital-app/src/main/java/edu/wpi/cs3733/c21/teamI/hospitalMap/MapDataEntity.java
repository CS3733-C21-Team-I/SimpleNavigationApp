package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathPlanningAlgorithm;
import edu.wpi.cs3733.c21.teamI.pathfinding.TextDirections;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MapDataEntity {

  public EuclidianDistCalc scorer = new EuclidianDistCalc();
  public PathPlanningAlgorithm pathFinderAlgorithm = new PathFinder();
  private List<HospitalMapNode> foundPath;

  private ArrayList<String> foundPathDescription = new ArrayList<>();

  public Map<String, HospitalMap> source;
  private Set<HospitalMapNode> hospitalMapNodes;
  private List<String> hospitalMapNodeNames;

  public MapDataEntity() {
    this.source = NavDatabaseManager.getInstance().loadMapsFromMemory();
  }

  // pathfinding functions
  public void clearFoundPath() {
    foundPath = null;
  }

  public boolean foundPathExists() {
    return foundPath != null && !foundPath.isEmpty();
  }

  public List<HospitalMapNode> getFoundPath() {
    if (foundPathExists()) {
      return foundPath;
    } else {
      return new ArrayList<HospitalMapNode>();
    }
  }

  public List<HospitalMapNode> getFoundPath(HospitalMapNode nodeA, HospitalMapNode nodeB) {
    if (foundPathExists()
        && foundPath.get(0).equals(nodeA)
        && foundPath.get(foundPath.size() - 1).equals(nodeB)) {
      System.out.println("Path already existed:" + this.foundPath);
      return foundPath;
    } else {
      this.foundPath = pathFinderAlgorithm.findPath(nodeA, nodeB, scorer);
      this.foundPathDescription = TextDirections.getDirections(scorer, foundPath);
      return foundPath;
    }
  }

  public ArrayList<String> getFoundPathDescription(List<HospitalMapNode> path) {
    if (!path.equals(foundPath)) {
      foundPathDescription = TextDirections.getDirections(scorer, path);
    }
    return foundPathDescription;
  }

  public ArrayList<String> getFoundPathDescription() {
    return foundPathDescription;
  }

  public void lookupNodes(KeyEvent e, ListView listView, TextField target) {
    String matchString =
        (((TextField) e.getSource()).getText()
                + (!e.getCharacter().equals(Character.toString((char) 8)) ? e.getCharacter() : ""))
            .toLowerCase();
    if (hospitalMapNodeNames == null) {
      hospitalMapNodeNames = new ArrayList<>();
      hospitalMapNodeNames.addAll(NavDatabaseManager.getInstance().getLocationNodeLongNames());
    }
    List<String> nodeNames = hospitalMapNodeNames;

    List<String> matches = new ArrayList<>();
    for (String location : nodeNames) {
      if (location.toLowerCase().contains(matchString)) {
        matches.add(location);
      }
    }
    // Add elements to ListView
    ObservableList<String> items = FXCollections.observableArrayList(matches);
    listView.setItems(items);
    listView.setVisible(e.getSource() == target);
  }

  public HospitalMapNode getNodeByLongName(String longName) {
    if (hospitalMapNodes == null) {
      hospitalMapNodes = new HashSet<>();
      for (Map.Entry<String, HospitalMap> map : source.entrySet()) {
        hospitalMapNodes.addAll(source.get(map.getKey()).getNodes());
      }
    }
    Set<HospitalMapNode> nodesLookup = hospitalMapNodes;
    for (HospitalMapNode node : nodesLookup) {
      if (node.getClass() == LocationNode.class
          && ((LocationNode) node).getLongName().equals(longName)) {
        return (LocationNode) node;
      }
    }
    return null;
  }
}
