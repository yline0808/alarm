package net.ddns.myapplication.table;

public class Song {
    private String title;
    private String uri;
    private Boolean isSelected;

    public Song(String title, String uri) {
        this.title = title;
        this.uri = uri;
        this.isSelected = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
