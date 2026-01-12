package com.uniovi.sdi2526entrega121.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Representa un vehículo registrado en el sistema, con sus atributos técnicos,
 * estado de uso y relaciones con rutas y repostajes.
 */

@Entity
public class Vehicle {

    public enum FuelType {
        GASOLINA, DIESEL, MICROHIBRIDO, HIBRIDO, ELECTRICO, GLP, GNL
    }

    public enum Status{
        LIBRE,OCUPADO
    }

    @Id
    @GeneratedValue
    private Long id;

    private String plate;
    private String model;
    private String brand;
    private String chassisNumber;
    private FuelType fuelType;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Refuel> getRefuels() {
        return refuels;
    }

    public void setRefuels(List<Refuel> refuels) {
        this.refuels = refuels;
    }

    private double totalKilometers;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Route> routes;


    @OneToMany(mappedBy = "vehicle",  cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Refuel> refuels;

    public Vehicle() {}

    public Vehicle(String plate, String model, String brand, String chassisNumber, FuelType fuelType) {
        this.plate = plate;
        this.model = model;
        this.brand = brand;
        this.chassisNumber = chassisNumber;
        this.fuelType = fuelType;
        this.totalKilometers = 0;
        this.status = Status.LIBRE;

    }

    public double getTotalKilometers() {
        return routes.stream()
                .map(Route::getOdometerFinalValue)
                .filter(Objects::nonNull) // Filtra trayectos incompletos
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    public void setTotalKilometers(double totalKilometers) {
        this.totalKilometers = totalKilometers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fueltype) {
        this.fuelType = fueltype;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "model='" + model + '\'' +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", chassisNumber=" + chassisNumber + '\'' +
                ", fuelType=" + fuelType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
