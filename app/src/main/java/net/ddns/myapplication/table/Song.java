package net.ddns.myapplication.table;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String uri;

    public Song() {
    }

    public Song(String title, String uri) {
        this.title = title;
        this.uri = uri;
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

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", uri=" + uri +
                '}';
    }
}
