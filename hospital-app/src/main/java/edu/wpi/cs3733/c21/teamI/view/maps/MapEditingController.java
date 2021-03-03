package edu.wpi.cs3733.c21.teamI.view.maps;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamI.ApplicationDataController;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.pathfinding.BreadthFirstSearch;
import edu.wpi.cs3733.c21.teamI.pathfinding.DepthFirstSearch;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import edu.wpi.cs3733.c21.teamI.user.User;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URISyntaxException;

public class MapEditingController extends MapController{
    @FXML
    JFXComboBox algorithmPick;
    @FXML
    Button save, discard, undoButton, redoButton, adminMapToggle;
    protected HospitalMapNode movingNode;

    public void updateView() throws IOException {
        if (isFirstLoad) {
            isFirstLoad = false;
            setAddNodeHandler();
            undoButton.setOnAction(
                    e -> {
                        if (ViewManager.getDataCont().isUndoAvailable()) {
                            ViewManager.getDataCont().undo();
                        }
                        update();
                    });
            redoButton.setOnAction(
                    e -> {
                        if (ViewManager.getDataCont().isRedoAvailable()) {
                            ViewManager.getDataCont().redo();
                        }
                        update();
                    });
        }
        try {
            Image background =
                    new Image(
                            (getClass()
                                    .getResource(
                                            "/fxml/mapImages/" + ViewManager.getMapID().replace(" ", "") + ".png"))
                                    .toURI()
                                    .toString());
            mapImage.setImage(background);
            fullImgWidth = background.getWidth();
            fullImgHeight = background.getHeight();
            imgWidth = background.getWidth();
            imgHeight = background.getHeight();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        nodeMenu.setVisible(
                ViewManager.selectedInActiveMap() && ViewManager.getSelectedNode() != null && adminMap);
        undoButton.setVisible(false);
        redoButton.setVisible(false);
        update();
    }

    protected void update() {
        mapPane.getChildren().clear();
        drawSelectedNode();
        for (HospitalMapNode node : ViewManager.getEntityNodes()) {
            drawEdges(node);
        }
        for (HospitalMapNode node : ViewManager.getEntityNodes()) {
            makeNodeCircle(node);
        }
        if (ViewManager.getDataCont().isUndoAvailable()) {
            undoButton.setOpacity(1);
        } else {
            undoButton.setOpacity(0.2);
        }

        if (ViewManager.getDataCont().isRedoAvailable()) {
            redoButton.setOpacity(1);
        } else {
            redoButton.setOpacity(0.2);
        }
    }

    @FXML
    private void switchAlgorithm() {
        switch (algorithmPick.getValue().toString()) {
            case "Depth First":
                System.out.println("Making new Depth first...");
                data.pathFinderAlgorithm = new DepthFirstSearch();
                break;
            case "Breadth First":
                System.out.println("Making new Breadth first...");
                data.pathFinderAlgorithm = new BreadthFirstSearch();
                break;
            default:
                data.pathFinderAlgorithm = new PathFinder();
                break;
        }
    }

    @FXML
    public void initialize() throws IOException {
        floor1Tab(new ActionEvent());
        campusTab(new ActionEvent());
        adminMapToggle.setVisible(true);
        algorithmPick.setVisible(true);
        algorithmPick.getItems().addAll("A*", "Depth First", "Breadth First");
        ViewManager.setMapController(this);
        setupMapViewHandlers();
    }
}
