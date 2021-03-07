package edu.wpi.cs3733.c21.teamI.GoogleMaps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.FindPlaceFromText;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class GoogleMapsController extends Application {
  @FXML private JFXButton submitButton;
  @FXML private JFXTextField addressSearch;
  @FXML private ListView autofillView;
  @FXML private Hyperlink mapLink;

  String API_KEY = "AIzaSyDffrpgnSe9viRPYC1v_u6qth7IL5ufPJE";
  String url = "https://maps.googleapis.com/maps/api/place/autocomplete/output";
  GeoApiContext context;
  PlaceAutocompleteRequest.SessionToken token;
  String destination =
      "Brigham and Women's Faulkner Hospital, Centre Street, Jamaica Plain, MA, USA";
  String destinationID = "";
  HashMap<String, String> idLookUp;

  @FXML
  public void initialize() {
    idLookUp = new HashMap<>();
    context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    token = new PlaceAutocompleteRequest.SessionToken();
    setupRequestView();
    mapLink.setVisible(false);
    destinationID = searchForPlaceID(destination);
  }

  @FXML
  public void submit(ActionEvent e) {
    String search = addressSearch.getText();
    getDirections(search);

    // Checks if location has a known id, otherwise, it searches for it
    String originID = "";
    if (idLookUp.containsKey(search)) {
      originID = idLookUp.get(search);
      //System.out.println("Used known ID: " + originID);
    } else originID = searchForPlaceID(search);

    mapLink.setText(formDirectionsUrl(originID, destinationID));
    // mapLink = new Hyperlink(formDirectionsUrl(search, destination));
    mapLink.setVisible(true);
    mapLink.setVisited(false);
  }

  @FXML
  public void clickLink(ActionEvent e) {
    System.out.println("going to link!");
    getHostServices().showDocument(mapLink.getText());
  }

  @FXML
  public void autoComplete() {
    String currText = addressSearch.getText();

    AutocompletePrediction[] result =
        PlacesApi.placeAutocomplete(context, currText, token).awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(result[0].description));
    List<String> suggestions = new ArrayList<>();

    for (int i = 0; i < result.length; i++) {
      suggestions.add(result[i].description);
      idLookUp.put(result[i].description, result[i].placeId);
    }

    ObservableList<String> items = FXCollections.observableArrayList(suggestions);
    autofillView.setItems(items);
    autofillView.setVisible(true);
  }

  private void setupRequestView() {
    autofillView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (ov, oldVal, newVal) -> {
                  addressSearch.setText(newVal);
                  // serviceLocationList.setVisible(false);
                });
  }

  public void getDirections(String origin) {
    DirectionsResult result =
        DirectionsApi.getDirections(context, origin, destination).awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(result));
  }

  private String formDirectionsUrl(String originID, String destinationID) {
    String url = "https://www.google.com/maps/dir/?api=1";

    url += "&origin=start&origin_place_id=" + originID;
    url += "&destination=end&destination_place_id=" + destinationID;

    System.out.println("URL:   " + url);
    return url;
  }

  private String searchForPlaceID(String location) {
    FindPlaceFromText result =
        PlacesApi.findPlaceFromText(
                context, location, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
            .awaitIgnoreError();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String id = result.candidates[0].placeId;

    System.out.println(id);
    return id;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
