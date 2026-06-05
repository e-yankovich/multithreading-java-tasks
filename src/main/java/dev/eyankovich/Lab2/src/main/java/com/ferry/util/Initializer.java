package com.ferry.util;

import com.ferry.core.Car;
import com.ferry.core.Truck;
import com.ferry.core.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Initializer {
    private static final Logger logger = LogManager.getLogger(Initializer.class);

    public static List<Vehicle> loadVehicles(String resourceName) {
        List<Vehicle> vehicles = new ArrayList<>();

        ClassLoader classLoader = Initializer.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                logger.error("The file is not found: {}", resourceName);
                return vehicles;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 4) {
                    logger.warn("String is missing: {}", line);
                    continue;
                }

                String type = parts[0];
                String id = parts[1];
                int weight = Integer.parseInt(parts[2]);
                int area = Integer.parseInt(parts[3]);

                switch (type) {
                    case "Car" -> vehicles.add(new Car(id, weight, area));
                    case "Truck" -> vehicles.add(new Truck(id, weight, area));
                    default -> logger.warn("Unknown type of transport: {}", type);
                }
            }

            logger.info("Loaded {} vehicles from {}", vehicles.size(), resourceName);

        } catch (Exception e) {
            logger.error("En error occurred while loading the file {}: {}", resourceName, e.getMessage(), e);
        }

        return vehicles;
    }
}
