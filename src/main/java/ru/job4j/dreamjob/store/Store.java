package ru.job4j.dreamjob.store;

import java.util.List;

public interface Store<T> {

    T add(T item);

    List<T> findAll();

    void update(T item);

    T findById(int id);
}
