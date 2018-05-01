package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static sample.Database.connect;


public class Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton getNewJoke;

    @FXML
    private JFXButton getSavedJoke;

    @FXML
    private JFXButton saveJoke;
    @FXML
    private Label jokeText;




    private Joke currentJoke;

    public boolean isDisplayedJokeSaved;

    private ArrayList<Joke> jokeList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isDisplayedJokeSaved = false;
        Database db = new Database();
        Connection conn = connect();
        Joke jokeObject = Database.getNewJoke();
        String joke = jokeObject.getValue();
        jokeText.setText(joke);
        db.readJokes(conn);
        jokeList.addAll(db.getJokeObjects());
        currentJoke = jokeObject;

        getNewJoke.setOnAction((e -> {
            Joke newJokeObject = Database.getNewJoke();
            String newJoke = newJokeObject.getValue();
            jokeText.setText(newJoke);
            currentJoke = newJokeObject;
            isDisplayedJokeSaved = false;
        }));

        saveJoke.setOnAction((e -> {


            if (!isDisplayedJokeSaved) {
                Database.insertNewJoke(conn, currentJoke);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Joke successfully saved into database.");
                Optional<ButtonType> result = alert.showAndWait();
                jokeList.add(currentJoke);
            }
            if (isDisplayedJokeSaved) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("This joke has already been saved.");
                Optional<ButtonType> result = alert.showAndWait();
            }

        }));

        getSavedJoke.setOnAction((e -> {
            String result = jokeList.get((int) (Math.random() * jokeList.size())).getValue();
            jokeText.setText(result);
            isDisplayedJokeSaved = true;

        }));



        }

    }


