package ru.job4j.dreamjob.service.photoservice;

public interface PhotoService {

    String PATH = "./src/main/java/ru/job4j/dreamjob/services/photoservice/photos/";

    boolean addPhoto(int id, byte[] file);
    void savePhoto(int id, byte[] photo);

    byte[] findById(int id);

    void updatePhoto(int id, byte[] file);
    void loadPhotos();
}
