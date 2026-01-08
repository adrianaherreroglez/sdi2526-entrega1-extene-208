package com.uniovi.sdi2425entrega121.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Representa un registro de repostaje de un vehículo, incluyendo datos como
 * la estación, el precio por unidad, la cantidad, si se llenó el depósito,
 * el odómetro, observaciones y la fecha del repostaje.
 */

@Entity
public class Refuel {

    @Id
    @GeneratedValue
    private Long id;
    private String stationName;
    private double pricePerUnit;
    private double totalPrice;
    private double quantity;
    private boolean isTankFull;

    private double odometerRefuelValue;
    private String observations;

    private Date refuelDate;

    @ManyToOne
    @JoinColumn(name="car_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Vehicle vehicle;

    public Refuel(String psn, double pppu, double q, boolean isTankFull, double o, String obs){
        this.stationName = psn;
        this.pricePerUnit = pppu;
        this.quantity = q;
        this.isTankFull = isTankFull;
        this.odometerRefuelValue = o;
        this.observations = obs;
        this.refuelDate = new Date();
    }

    public Refuel(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice(){
        return totalPrice;
    }

    public double getOdometerRefuelValue() {
        return odometerRefuelValue;
    }

    public void setOdometerRefuelValue(double odometerRefuelValue) {
        this.odometerRefuelValue = odometerRefuelValue;
    }

    public boolean getIsTankFull() {
        return isTankFull;
    }

    public void setIsTankFull(boolean isTankFull) {
        this.isTankFull = isTankFull;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observaciones) {
        this.observations = observaciones;
    }

    public Date getRefuelDate() {
        return refuelDate;
    }

    public void setRefuelDate(Date refuelDate) {
        this.refuelDate = refuelDate;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle.FuelType getFuelType(){
        return this.vehicle.getFuelType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Refuel refuel = (Refuel) o;
        return Objects.equals(id, refuel.id);
    }

    @Override
    public String toString() {
        return "Refuel{" +
                "stationName='" + stationName + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", quantity=" + quantity +
                ", isTankFull=" + isTankFull +
                ", totalPrice=" + totalPrice +
                ", odometro='" + odometerRefuelValue + '\'' +
                ", observaciones='" + observations + '\'' +
                ", refuelDate=" + refuelDate +
                ", vehicle=" + vehicle +
                '}';
    }
}
