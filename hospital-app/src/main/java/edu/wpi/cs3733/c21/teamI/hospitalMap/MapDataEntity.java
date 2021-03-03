package edu.wpi.cs3733.c21.teamI.hospitalMap;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import java.util.*;

public class MapDataEntity {
  private static Map<String, HospitalMap> sourceMap;
  private static Set<HospitalMapNode> hospitalMapNodes;

  public static Map<String, HospitalMap> getMap(boolean refresh) {
    if (refresh || sourceMap == null) {
      sourceMap = NavDatabaseManager.getInstance().loadMapsFromMemory();
    }
    return sourceMap;
  }

  public static Map<String, HospitalMap> getMap() {
    return getMap(false);
  }

  public static Set<HospitalMapNode> getNodesSet(boolean refresh) {
    if (refresh || hospitalMapNodes == null) {
      hospitalMapNodes = new HashSet<>();
      for (Map.Entry<String, HospitalMap> map : getMap().entrySet()) {
        hospitalMapNodes.addAll(getMap(false).get(map.getKey()).getNodes());
      }
    }
    return hospitalMapNodes;
  }

  public static Set<HospitalMapNode> getNodesSet() {
    return getNodesSet(false);
  }

  public static HospitalMapNode getNodeByLongName(String longName) {
    Set<HospitalMapNode> nodesLookup = getNodesSet(false);
    for (HospitalMapNode node : nodesLookup) {
      if (node.getClass() == LocationNode.class
          && ((LocationNode) node).getLongName().equals(longName)) {
        return (LocationNode) node;
      }
    }
    return null;
  }
}
