package sample;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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

    @FXML
    private ProgressBar progressBar;


    private Joke currentJoke;

    public boolean isDisplayedJokeSaved;

    private ArrayList<Joke> jokeList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressBar.setVisible(false);
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
            progressBar.setVisible(true);
            PauseTransition pt = new PauseTransition();
            pt.setDuration(Duration.seconds(1));
            pt.setOnFinished((ev -> {
                Joke newJokeObject = Database.getNewJoke();
                String newJoke = newJokeObject.getValue();
                jokeText.setText(newJoke);
                currentJoke = newJokeObject;
                isDisplayedJokeSaved = false;
                progressBar.setVisible(false);
            }));
            pt.play();

        }));

        saveJoke.setOnAction((e -> {


            if (!isDisplayedJokeSaved) {
                Database.insertNewJoke(conn, currentJoke);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Joke successfully saved into database.");
                Optional<ButtonType> result = alert.showAndWait();
                jokeList.add(currentJoke);
                isDisplayedJokeSaved = true;


            }
            else if (isDisplayedJokeSaved) {
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


