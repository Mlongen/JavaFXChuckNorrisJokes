package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private ArrayList<Joke> jokeObjects;

    public Database() {
        this.jokeObjects = new ArrayList<>();
    }

    public ArrayList<Joke> getJokeObjects() {
        return jokeObjects;
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:jokes.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite database successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void readJokes(Connection conn) {
        String query = "SELECT id, category, icon_url, joke_url, value FROM favorite";
        try {
            // a. Create a statement
            Statement stmt = conn.createStatement();
            //b. Execute the query -> returns a ResultSet
            ResultSet rs = stmt.executeQuery(query); //Iterator


            while (rs.next()) {
                Joke jk = new Joke(rs.getString("id"), rs.getString("category"), rs.getString("icon_url"), rs.getString("joke_url"), rs.getString("value"));

                jokeObjects.add(jk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Joke getNewJoke() {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.chucknorris.io/jokes/random").asJson();


            int status = jsonResponse.getStatus();
            if (status == 200 || status == 201) {
                JsonNode node = jsonResponse.getBody();
                JSONObject obj = node.getObject();
                String category;
                if (obj.isNull("category")) {
                    category = null;
                } else {
                    JSONArray ctgry = obj.getJSONArray("category");
                    category = ctgry.getString(0);
                }
                String icon_url = obj.getString("icon_url");
                String id = obj.getString("id");
                String url = obj.getString("url");
                String value = obj.getString("value");
                Joke random_joke;
                if (category == null) {
                    random_joke = new Joke("General", icon_url, id, url, value);
                    return random_joke;

                } else {
                    random_joke = new Joke(category, icon_url, id, url, value);
                    return random_joke;

                }

            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void insertNewJoke(Connection conn, Joke jk) {
        String id = jk.getId();
        String category = jk.getCategory();
        String icon_url = jk.getIcon_url();
        String joke_url = jk.getJoke_url();
        String value = jk.getValue();

        //-----------------------
        String query = "INSERT INTO favorite(id, category, icon_url, joke_url, value) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);  //Creates a prepared statement  based on your Query str
            pstmt.setString(1, id);
            pstmt.setString(2, category);
            pstmt.setString(3, icon_url);
            pstmt.setString(4, joke_url);
            pstmt.setString(5, value);
            pstmt.executeUpdate();

            System.out.println("Successfully inserted a new joke");

        } catch (SQLException z) {
            z.printStackTrace();
        }
    }






}
