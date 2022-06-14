package com.example.car.controller;

import com.example.car.model.Car;
import com.example.car.repo.CarRepo;
import com.example.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    CarService carService;

    @GetMapping()
    public @ResponseBody CompletableFuture<List<Car>> getAll() {
        return carService.getAllCars();
    }

    @PostMapping("/upload")
    public ResponseEntity<Car> uploadFile(@RequestParam (value = "files")MultipartFile[] files ) {
        try {
            for(final MultipartFile file:files) {
                carService.saveCars(file);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
