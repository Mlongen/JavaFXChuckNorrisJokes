package sample;

public class Joke {

    private String category;
    private String icon_url;
    private String id;
    private String joke_url;
    private String value;

    public Joke(String category, String icon_url, String id, String joke_url, String value) {
        this.category = category;
        this.icon_url = icon_url;
        this.id = id;
        this.joke_url = joke_url;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getIcon_url() {
        return icon_url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoke_url() {
        return joke_url;
    }



    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Joke{" + "value='" + value + '\'' + '}';
    }
}