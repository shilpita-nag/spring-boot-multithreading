package com.example.car.service;

import com.example.car.model.Car;
import com.example.car.repo.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CarService {

    @Autowired
    CarRepo carRepo;

    @Async
    public CompletableFuture<List<Car>> getAllCars() {
        final List<Car> allCars = carRepo.findAll();
        return CompletableFuture.completedFuture(allCars);
    }

    @Async
    public CompletableFuture<List<Car>> saveCars(final MultipartFile multipartFile) throws Exception{
        final long start = System.currentTimeMillis();
        System.out.println("start" + start);
        List<Car> cars = parseCsv(multipartFile);
        cars = carRepo.saveAll(cars);
        System.out.println("end" + (System.currentTimeMillis()-start));
        return CompletableFuture.completedFuture(cars);
    }

    public List<Car> parseCsv(final MultipartFile file) throws Exception{
        final List<Car> cars = new ArrayList<>();
        try {
            try(final BufferedReader read = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while((line = read.readLine()) != null) {
                    final String[] dataLine = line.split(";");
                    final Car car = new Car();
                    car.setModel(dataLine[0]);
                    car.setManufacturer(dataLine[1]);
                    car.setType(dataLine[2]);
                    cars.add(car);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return cars;
    }
}
