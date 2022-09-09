package ru.job4j.dreamjob.service.cityservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService {

    private final Map<Integer, City> cities = new HashMap<>();

    public CityServiceImpl(@Value("${city-names}") List<String> cityNames) {
        init(cityNames);
    }

    private void init(List<String> names) {
        for (int i = 0; i < names.size(); i++) {
            cities.put(i + 1, new City(i + 1, names.get(i)));
        }
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
