package models;
// ========== CLASSE TRACK ================
// versione 2.0 ( Aggiunta identificativi )

import com.fasterxml.jackson.annotation.JsonProperty;
import util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private String id;
    private String pathname;
    private String name;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private boolean favourite;
    private boolean explicit;
    private int duration; // in secondi
    private int timesListened;


    private List<String> tags = new ArrayList<>();

    // Costruttore vuoto
    public Track() {}

    // Costruttore completo
    public Track(String pathname, String name, String artist, String album, String genre,
                 int year, boolean favourite, boolean explicit, int duration) {
        this.id = IdGenerator.generateId();
        System.out.println(id);
        this.pathname = pathname;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.favourite = favourite;
        this.explicit = explicit;
        this.duration = duration;
        this.timesListened = 0;
        this.tags = new ArrayList<>();
    }

    // Getters e Setters
    public String getId(){ return id;}

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

    public int getTimesListened(){ return timesListened; };

    public void setTimesListened(){ this.timesListened++;}

    // 2. Getter standard
    public List<String> getTags() {
        return tags;
    }

    // 3. Setter (necessario per il caricamento da JSON)
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // 4. Metodo per aggiungere un tag (con controllo duplicati)
    public void addTag(String tag) {

        if (tag != null && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }

    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }

        if (!tags.contains(tag)) {
            tags.add(String.valueOf(tag));
        }
    }

    // 5. Metodo per rimuovere un tag (utile per deselezionare il tag nell'UI)
    public void removeTag(String tag) {
        this.tags.remove(tag);
    }


    /** viene usato Objects.equals(...) per gestire automaticamente i null
     * il metodo si occupa di verificare se due oggetti (Track) sono uguali
     * usando come paragone il solo id
     * @params obj la traccia con cui effettuare la verifica di uguaglianza
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Track)) return false;

        Track t = (Track) obj;

        return year == t.year &&
                favourite == t.favourite &&
                explicit == t.explicit &&
                duration == t.duration &&
                java.util.Objects.equals(id, t.id) &&
                java.util.Objects.equals(pathname, t.pathname) &&
                java.util.Objects.equals(name, t.name) &&
                java.util.Objects.equals(artist, t.artist) &&
                java.util.Objects.equals(album, t.album) &&
                java.util.Objects.equals(genre, t.genre);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                id,
                pathname,
                name,
                artist,
                album,
                genre,
                year,
                favourite,
                explicit,
                duration
        );
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album=" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", favourite=" + favourite +
                ", explicit=" + explicit +
                ", duration=" + duration +
                '}';
    }
}