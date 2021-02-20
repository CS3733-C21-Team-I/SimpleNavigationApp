package edu.wpi.cs3733.c21.teamI.hospitalMap.mapEditing;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMap;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import java.util.Map;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MapEditManager {

  private static MapEditManager ourInstance;
  private Map<String, HospitalMap> mapCollection;
  private double scale = 3.05; // scales image to 1/scale
  private MapEditView mapEditorView = null;
  private ApplicationView applicationView = null;
  protected AnchorPane mapPane = null;
  private Group root = null;
  private Stage stage = null;
  private HospitalMapNode selectedNode = null;
  private MapEditDataController dataCont;

  public static void init() {
    ourInstance = new MapEditManager();
  }

  public void setMapCollection(Map<String, HospitalMap> mapCollection) {
    this.mapCollection = mapCollection;
  }

  public static MapEditManager getInstance() {
    return ourInstance;
  }

  public MapEditManager() {
    dataCont = new MapEditDataController();
  }

  /**
   * takes in a node object and assigns / de-assigns it to the selectedNode variable in MapState
   *
   * @param node the node to be assigned or de-assigned
   */
  public void toggleNode(HospitalMapNode node) {
    if (selectedNode == null) {
      selectedNode = node;
    } else if (selectedNode.equals(node)) {
      selectedNode = null;
    } else {
      dataCont.addEdge(node.getID(), selectedNode.getID());
      selectedNode = null;
    }
  }

  public double getScale() {
    return scale;
  }

  public Set<HospitalMapNode> getEntityNodes() {
    return dataCont.getActiveMap().getNodes();
  }

  public void startEditorView(AnchorPane mapPane) {
    this.mapPane = mapPane;
    mapEditorView = new MapEditView(this);
    mapEditorView.start(stage);
    mapEditorView.saveManager();
  }

  public void startApplicationView() {
    this.root = new Group();
    applicationView = new ApplicationView(this);
    applicationView.saveManager();
  }

  public void setRoot(Group root) {
    this.root = root;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

  public Group getRoot() {
    return root;
  }

  public String getMapID() {
    return dataCont.getActiveMap().getId();
  }

  public HospitalMapNode getSelectedNode() {
    return selectedNode;
  }

  public MapEditDataController getDataCont() {
    return dataCont;
  }
}
