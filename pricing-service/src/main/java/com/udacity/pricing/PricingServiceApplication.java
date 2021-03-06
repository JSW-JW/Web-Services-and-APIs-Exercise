package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import com.udacity.pricing.service.PriceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 * TODO: Convert the application from a REST API to a microservice.
 */
@SpringBootApplication
@EnableEurekaClient
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

//    /**
//     * Holds {ID: Price} pairings (current implementation allows for 20 vehicles)
//     */
//    private static final Map<Long, Price> PRICES = LongStream
//            .range(1, 21)
//            .mapToObj(i -> new Price("USD", randomPrice(), i))
//            .collect(Collectors.toMap(Price::getVehicleId, p -> p));
//
//    /**
//     * If a valid vehicle ID, gets the price of the vehicle from the stored array.
//     * @param vehicleId ID number of the vehicle the price is requested for.
//     * @return price of the requested vehicle
//     * @throws PriceException vehicleID was not found
//     */
//    public static Price getPrice(Long vehicleId) throws PriceException {
//
//        if (!PRICES.containsKey(vehicleId)) {
//            throw new PriceException("Cannot find price for Vehicle " + vehicleId);
//        }
//
//        return PRICES.get(vehicleId);
//    }
//
//    /**
//     * Gets a random price to fill in for a given vehicle ID.
//     * @return random price for a vehicle
//     */
//    private static BigDecimal randomPrice() {
//        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
//                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
//    }
//
//    @Bean
//    CommandLineRunner initDatabase(PriceRepository repository) {
//        return args -> {
//            for (Long l=1L; l<= 20; l++) {
//                Price price = PRICES.get(l);
//                repository.save(price);
//            }
//        };
//    }

}
