package edu.wpi.cs3733.c21.teamI.view;

import edu.wpi.cs3733.c21.teamI.database.NavDatabaseManager;
import edu.wpi.cs3733.c21.teamI.hospitalMap.EuclidianDistCalc;
import edu.wpi.cs3733.c21.teamI.hospitalMap.HospitalMapNode;
import edu.wpi.cs3733.c21.teamI.hospitalMap.LocationNode;
import edu.wpi.cs3733.c21.teamI.view.MapEditManager;
import edu.wpi.cs3733.c21.teamI.pathfinding.PathFinder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MapController extends Application {
	MapEditManager mapManager;
	boolean adminMap = false;

	@FXML
	Button
			save,
			discard,
			undoButton,
			redoButton;

	@FXML
	TextField start, destination;

	@FXML
	AnchorPane mapPane;

	private final double scale = 3.05;
	private EuclidianDistCalc scorer;
	@FXML
	public void toggleEditMap(ActionEvent e) {
		adminMap = !adminMap;
		if (adminMap) {
			System.out.println("Starting Map Edit");
			mapManager
					.getDataCont()
					.setActiveMap(NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0"));
			mapManager.startEditorView(mapPane);
			undoButton.setVisible(true);
			redoButton.setVisible(true);
		} else {
			mapManager.setNodeMenuVisible(false);
			mapManager.getDataCont().discardChanges();
			undoButton.setVisible(false);
			redoButton.setVisible(false);
		}
		mapPane.setVisible(adminMap);
		save.setVisible(adminMap);
		discard.setVisible(adminMap);
	}

	public void navigate(ActionEvent e) throws IOException {ApplicationView.navigate(e);}

	@FXML
	public void getDirections(ActionEvent e) {
		String begin = start.getText();
		String end = destination.getText();
		drawPathBetweenNodes(begin, end);
	}

	public LocationNode getNodeByLongName(String longName) {
		for (HospitalMapNode node :
				NavDatabaseManager.getInstance().loadMapsFromMemory().get("Faulkner 0").getNodes()) {
			if (node.getClass() == LocationNode.class
					&& ((LocationNode) node).getLongName().equals(longName)) {
				return (LocationNode) node;
			}
		}
		return null;
	}

	@FXML
	public void deletePath() {
		mapPane.getChildren().clear();
	}

	@FXML
	public void drawPathBetweenNodes(String aName, String bName) {
		deletePath();
		mapManager
				.getRoot()
				.getChildren()
				.removeIf(n -> (n.getClass() == Line.class) || (n.getClass() == Circle.class));

		this.scorer = new EuclidianDistCalc();
		HospitalMapNode nodeA = getNodeByLongName(aName);
		HospitalMapNode nodeB = getNodeByLongName(bName);
		List<HospitalMapNode> aStarPath = PathFinder.findPath(nodeA, nodeB, scorer);
		drawPath(aStarPath);
		drawNode(nodeA, Color.BLUE);
		drawNode(nodeB, Color.BLUE);
	}

	private void drawNode(HospitalMapNode node, Color color) {
		Circle circle =
				new Circle((node.getxCoord() / scale) - 3, (node.getyCoord() / scale), 13 / scale);
		circle.setFill(color);
		mapPane.getChildren().add(circle);
	}

	private void drawEdge(HospitalMapNode start, HospitalMapNode end, Color color) {
		Line line =
				LineBuilder.create()
						.startX((start.getxCoord() / scale) - 3)
						.startY((start.getyCoord() / scale))
						.endX((end.getxCoord() / scale) - 3)
						.endY((end.getyCoord() / scale))
						.stroke(color)
						.strokeWidth(14 / scale)
						.build();
		mapPane.getChildren().add(line);
	}

	public void drawPath(List<HospitalMapNode> path) {
		HospitalMapNode currNode;
		HospitalMapNode nextNode = null;
		for (int i = 0; i < path.size() - 1; i++) {
			currNode = path.get(i);
			nextNode = path.get(i + 1);
			drawEdge(currNode, nextNode, Color.BLUE);
		}
	}

	@FXML
	public void saveChanges() {
		mapManager.getDataCont().saveChanges();
		adminMap = !adminMap;
		if (adminMap) {
			mapManager.startEditorView(mapPane);
		} else {
			mapManager.setNodeMenuVisible(false);
		}
		mapPane.setVisible(adminMap);
		save.setVisible(adminMap);
		discard.setVisible(adminMap);
	}

	@FXML
	public void discardChanges() {
		mapManager.getDataCont().discardChanges();
		adminMap = !adminMap;
		if (adminMap) {
			mapManager.startEditorView(mapPane);
		} else {
			mapManager.setNodeMenuVisible(false);
		}
		mapPane.setVisible(adminMap);
		save.setVisible(adminMap);
		discard.setVisible(adminMap);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
