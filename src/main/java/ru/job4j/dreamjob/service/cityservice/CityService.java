package ru.job4j.dreamjob.service.cityservice;

import ru.job4j.dreamjob.model.City;

import java.util.List;

public interface CityService {

    List<City> getAllCities();

    City findById(int id);
}
