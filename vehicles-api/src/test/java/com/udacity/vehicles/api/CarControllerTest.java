package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarNotFoundException;
import com.udacity.vehicles.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);

        given(carService.list()).willReturn(Collections.singletonList(car));

        given(priceClient.getPrice(any())).willReturn("USD 12324.22");
        Location serviceLocation = new Location(40.730610, -73.935242);
        serviceLocation.setAddress("10040 County Road 48");
        serviceLocation.setCity("Fairhope");
        serviceLocation.setState("AL");
        serviceLocation.setZip("36533");
        given(mapsClient.getAddress(any())).willReturn(serviceLocation);
    }

    /**
     * Tests for successful creation of new car in the system
     *
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        car.setId(1L);

        given(carService.save(any())).willReturn(car);

        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.details.body", is(car.getDetails().getBody())))
                .andExpect(jsonPath("$.details.model", is(car.getDetails().getModel())))
                .andExpect(jsonPath("$.details.manufacturer.code", is(car.getDetails().getManufacturer().getCode())))
                .andExpect(jsonPath("$.details.manufacturer.name", is(car.getDetails().getManufacturer().getName())))
                .andExpect(jsonPath("$.details.numberOfDoors", is(car.getDetails().getNumberOfDoors())))
                .andExpect(jsonPath("$.details.fuelType", is(car.getDetails().getFuelType())))
                .andExpect(jsonPath("$.details.mileage", is(car.getDetails().getMileage())))
                .andExpect(jsonPath("$.details.modelYear", is(car.getDetails().getModelYear())))
                .andExpect(jsonPath("$.details.productionYear", is(car.getDetails().getProductionYear())))
                .andExpect(jsonPath("$.details.externalColor", is(car.getDetails().getExternalColor())))
                .andExpect(jsonPath("$.details.engine", is(car.getDetails().getEngine())))
                .andExpect(jsonPath("$.id", is(1)));
        verify(carService, Mockito.times(1)).save(any(Car.class));
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   the whole list of vehicles. This should utilize the car from `getCar()`
         *   below (the vehicle will be the first in the list).
         */
        Car car = getCar();
        car.setId(1L);
        Location location = mapsClient.getAddress(new Location(car.getLocation().getLat(), car.getLocation().getLon()));
        String price = priceClient.getPrice(car.getId());
        car.setLocation(location);
        car.setPrice(price);
        given(carService.list()).willReturn(Collections.singletonList(car));


        String root = "$._embedded.carList[0]";
        mvc.perform(
                get(new URI("/cars"))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(root + ".details.body", is(car.getDetails().getBody())))
                .andExpect(jsonPath(root + ".details.model", is(car.getDetails().getModel())))
                .andExpect(jsonPath(root + ".details.manufacturer.code", is(car.getDetails().getManufacturer().getCode())))
                .andExpect(jsonPath(root + ".details.manufacturer.name", is(car.getDetails().getManufacturer().getName())))
                .andExpect(jsonPath(root + ".details.numberOfDoors", is(car.getDetails().getNumberOfDoors())))
                .andExpect(jsonPath(root + ".details.fuelType", is(car.getDetails().getFuelType())))
                .andExpect(jsonPath(root + ".details.mileage", is(car.getDetails().getMileage())))
                .andExpect(jsonPath(root + ".details.modelYear", is(car.getDetails().getModelYear())))
                .andExpect(jsonPath(root + ".details.productionYear", is(car.getDetails().getProductionYear())))
                .andExpect(jsonPath(root + ".details.externalColor", is(car.getDetails().getExternalColor())))
                .andExpect(jsonPath(root + ".details.engine", is(car.getDetails().getEngine())))
                .andExpect(jsonPath(root + ".price", is(car.getPrice())))
                .andExpect(jsonPath(root + ".location.address", is(car.getLocation().getAddress())))
                .andExpect(jsonPath(root + ".location.city", is(car.getLocation().getCity())))
                .andExpect(jsonPath(root + ".location.state", is(car.getLocation().getState())))
                .andExpect(jsonPath(root + ".location.zip", is(car.getLocation().getZip())))
                .andExpect(jsonPath(root + ".id", is(1)));
        verify(carService, Mockito.times(1)).list();
    }

    /**
     * Tests the read operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   a vehicle by ID. This should utilize the car from `getCar()` below.
         */
        Car car = getCar();
        car.setId(1L);
        Location location = mapsClient.getAddress(new Location(car.getLocation().getLat(), car.getLocation().getLon()));
        String price = priceClient.getPrice(car.getId());
        car.setLocation(location);
        car.setPrice(price);
        given(carService.findById(any())).willReturn(car);

        mvc.perform(
                get("/cars/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details.body", is(car.getDetails().getBody())))
                .andExpect(jsonPath("$.details.model", is(car.getDetails().getModel())))
                .andExpect(jsonPath("$.details.manufacturer.code", is(car.getDetails().getManufacturer().getCode())))
                .andExpect(jsonPath("$.details.manufacturer.name", is(car.getDetails().getManufacturer().getName())))
                .andExpect(jsonPath("$.details.numberOfDoors", is(car.getDetails().getNumberOfDoors())))
                .andExpect(jsonPath("$.details.fuelType", is(car.getDetails().getFuelType())))
                .andExpect(jsonPath("$.details.mileage", is(car.getDetails().getMileage())))
                .andExpect(jsonPath("$.details.modelYear", is(car.getDetails().getModelYear())))
                .andExpect(jsonPath("$.details.productionYear", is(car.getDetails().getProductionYear())))
                .andExpect(jsonPath("$.details.externalColor", is(car.getDetails().getExternalColor())))
                .andExpect(jsonPath("$.details.engine", is(car.getDetails().getEngine())))
                .andExpect(jsonPath("$.price", is(car.getPrice())))
                .andExpect(jsonPath("$.location.address", is(car.getLocation().getAddress())))
                .andExpect(jsonPath("$.location.city", is(car.getLocation().getCity())))
                .andExpect(jsonPath("$.location.state", is(car.getLocation().getState())))
                .andExpect(jsonPath("$.location.zip", is(car.getLocation().getZip())))
                .andExpect(jsonPath("$.id", is(1)));
        verify(carService, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    public void updateCar() throws Exception {
        Car carBefore = getCar();
        carBefore.setId(1L);

        Car carAfter = getCar();
        carAfter.setId(1L);

        given(carService.findById(any())).willReturn(carBefore);
        // set return object 'carBefore' for the FindById() method

        mvc.perform(
                get("/cars/{id}", 1L))
                .andDo(print())
                .andExpect(jsonPath("$.details.fuelType", is(carBefore.getDetails().getFuelType())))
                .andExpect(jsonPath("$.details.body", is(carBefore.getDetails().getBody())))
                .andExpect(jsonPath("$.details.externalColor", is(carBefore.getDetails().getExternalColor())))
                .andExpect(status().isOk());

        verify(carService, Mockito.times(1)).findById(any(Long.class));

        carAfter.getDetails().setFuelType("Hydrogen");
        carAfter.getDetails().setBody("Updated Car");
        carAfter.getDetails().setExternalColor("SuperDuper color");
        given(carService.save(any())).willReturn(carAfter);
        // set return object 'carAfter' before execute update request and perform mockMvc put method.

        mvc.perform(
                put("/cars/{id}", 1L)
                        .content(json.write(carAfter).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details.body", is(carAfter.getDetails().getBody())))
                .andExpect(jsonPath("$.details.model", is(carAfter.getDetails().getModel())))
                .andExpect(jsonPath("$.details.manufacturer.code", is(carAfter.getDetails().getManufacturer().getCode())))
                .andExpect(jsonPath("$.details.manufacturer.name", is(carAfter.getDetails().getManufacturer().getName())))
                .andExpect(jsonPath("$.details.numberOfDoors", is(carAfter.getDetails().getNumberOfDoors())))
                .andExpect(jsonPath("$.details.fuelType", is(carAfter.getDetails().getFuelType())))
                .andExpect(jsonPath("$.details.mileage", is(carAfter.getDetails().getMileage())))
                .andExpect(jsonPath("$.details.modelYear", is(carAfter.getDetails().getModelYear())))
                .andExpect(jsonPath("$.details.productionYear", is(carAfter.getDetails().getProductionYear())))
                .andExpect(jsonPath("$.details.externalColor", is(carAfter.getDetails().getExternalColor())))
                .andExpect(jsonPath("$.details.engine", is(carAfter.getDetails().getEngine())))
                .andExpect(jsonPath("$.id", is(1)));
        verify(carService, Mockito.times(1)).save(any(Car.class));
    }

    /**
     * Tests the deletion of a single car by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        /**
         * TODO: Add a test to check whether a vehicle is appropriately deleted
         *   when the `delete` method is called from the Car Controller. This
         *   should utilize the car from `getCar()` below.
         */

        Car car = getCar();
        car.setId(1L);

        given(carService.save(any())).willReturn(car);

        mvc.perform(
                delete(new URI("/cars/1")))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(carService, Mockito.times(1)).delete(any(Long.class));

        if (carService.findById(1L) == null) {
            given(carService.findById(1L)).willThrow(CarNotFoundException.class);
        }
        assertThrows(CarNotFoundException.class, () -> carService.findById(1L));
    }

    /**
     * Creates an example Car object for use in testing.
     *
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}