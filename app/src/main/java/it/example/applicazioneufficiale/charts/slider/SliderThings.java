package it.example.applicazioneufficiale.charts.slider;

public class SliderThings {
    int id;
    String title;
    String ImageUrl;

    public SliderThings(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        ImageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
