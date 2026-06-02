package models;

public class Track {

    private String pathname;
    private String name;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private boolean favourite;
    private boolean explicit;
    private int duration; // in seconds

    // Empty constructor
   // public Track() {}

    // Full constructor
    public Track(String pathname, String name, String artist, String album, String genre,
                 String text, boolean favourite, boolean explicit, int duration) {
        this.pathname = pathname;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.favourite = favourite;
        this.explicit = explicit;
        this.duration = duration;
    }

    // Getters and Setters
    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum(){
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // viene usato Objects.equals(...) per gestire automaticamente i null
    public boolean equals(Track t) {
        return this.year == t.year &&
                this.favourite == t.favourite &&
                this.explicit == t.explicit &&
                this.duration == t.duration &&
                java.util.Objects.equals(this.name, t.name) &&
                java.util.Objects.equals(this.artist, t.artist) &&
                java.util.Objects.equals(this.album, t.album) &&
                java.util.Objects.equals(this.genre, t.genre);

    }
    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", favourite=" + favourite +
                ", explicit=" + explicit +
                ", duration=" + duration +
                '}';
    }
}