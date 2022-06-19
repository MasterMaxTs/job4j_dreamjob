package ru.job4j.dreamjob.services.photoservice;

public interface ManagePhoto {

    String PATH = "./src/main/java/ru/job4j/dreamjob/services/photoservice/photos/";
    void save(int id, byte[] photo);
    void loadPhotos();
}
