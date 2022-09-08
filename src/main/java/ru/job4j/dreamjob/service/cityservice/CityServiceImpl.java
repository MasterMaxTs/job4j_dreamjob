package ru.job4j.dreamjob.service.cityservice;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService {

    private final Map<Integer, City> cities = new HashMap<>();

    public CityServiceImpl() {
        cities.put(1, new City(1, "Москва"));
        cities.put(2, new City(2, "СПб"));
        cities.put(3, new City(3, "Екб"));
    }

    @Override
    public List<City> getAllCities() {
        return new ArrayList<>(cities.values());
    }

    @Override
    public City findById(int id) {
        return cities.get(id);
    }
}
