package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.TrackList;
import models.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryService implements TrackList {

    private final String FILE_PATH = "jsonfiles/tracks.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private List<Track> cache = new ArrayList<>();

    public LibraryService() {
        loadFromFile();
    }

    // ---------------- JSON ----------------

    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                System.out.println("Cianci");
                cache = new ArrayList<>();
                return;
            }

            cache = mapper.readValue(file, new TypeReference<List<Track>>() {});
        } catch (Exception e) {
            cache = new ArrayList<>();
        }
    }

    private void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- TrackList ----------------

    @Override
    public void addTrack(Track t) {
        cache.add(t);
        saveToFile();
    }

    @Override
    public void removeTrack(Track t) {
        cache.removeIf(x -> x.getName().equals(t.getName()));
        saveToFile();
    }

    @Override
    public List<Track> getTracks() {
        return cache;
    }

    @Override
    public int getSize() {
        return cache.size();
    }
}