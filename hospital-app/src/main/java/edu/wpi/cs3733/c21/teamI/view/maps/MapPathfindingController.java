package edu.wpi.cs3733.c21.teamI.view.maps;

import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.ticket.ServiceTicketDataController;
import edu.wpi.cs3733.c21.teamI.view.ViewManager;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MapPathfindingController extends MapController{
    @FXML
    TextField start, destination;
    @FXML
    ListView startList, destList, directionsField;

    public void updateView() throws IOException {
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
        mapPane.getChildren().clear();
        if (data.foundPathExists()) {
            ObservableList<String> items = FXCollections.observableArrayList(new ArrayList<String>());
            directionsField.setItems(items);
            drawCalculatedPath(data.getFoundPath());
        }
    }

    public void lookup(KeyEvent e) {
        if (e.getSource() == start) {
            ServiceTicketDataController.lookupNodes(e, startList, start);
        } else {
            ServiceTicketDataController.lookupNodes(e, destList, destination);
        }
    }

    protected void setupMapViewHandlers() {
        startList
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (ChangeListener<String>)
                                (ov, oldVal, newVal) -> {
                                    start.setText(newVal);
                                    startList.setVisible(false);
                                });
        destList
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (ChangeListener<String>)
                                (ov, oldVal, newVal) -> {
                                    destination.setText(newVal);
                                    destList.setVisible(false);
                                });
        mapPane.setOnMouseClicked(
                (MouseEvent evt) -> {
                    if (mapPane != null) {
                        startList.setVisible(false);
                        destList.setVisible(false);
                    }
                });
    }

    @FXML
    public void getDirections(ActionEvent e) throws IOException {
        String begin = start.getText();
        String end = destination.getText();
        if (begin.length() > 0 && end.length() > 0) {
            System.out.println(begin + " " + end);

            HospitalMapNode nodeA = data.getNodeByLongName(begin);
            HospitalMapNode nodeB = data.getNodeByLongName(end);
            data.getFoundPath(nodeA, nodeB);
            updateView();
        }
    }
}
