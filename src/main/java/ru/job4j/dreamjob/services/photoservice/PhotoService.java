package ru.job4j.dreamjob.services.photoservice;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PhotoService implements ManagePhoto {

    private final Map<Integer, byte[]> photos = new ConcurrentHashMap<>();

    public boolean addPhoto(int id, byte[] file) throws IOException {
        save(id, file);
        return photos.putIfAbsent(id, file) != null;
    }

    public void updatePhoto(int id, byte[] file) {
        photos.replace(id, file);
        save(id, file);
    }

    public byte[] findById(int id) {
        return photos.get(id);
    }

    @Override
    public void save(int id, byte[] photo) {
        final String extension = ".jpg";
        try {
            Files.write(Path.of(PATH + id + extension), photo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadPhotos() {
        File[] files = new File(PATH).listFiles();
        Arrays.stream(files)
                .map(File::getName)
                .forEach(fn -> {
                                int pos = fn.lastIndexOf(".");
                                String fileName = fn.substring(0, pos);
                                try {
                                    byte[] photo =
                                            Files.readAllBytes(Path.of(PATH + fn));
                                    photos.putIfAbsent(Integer.parseInt(fileName), photo);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                });
    }
}

