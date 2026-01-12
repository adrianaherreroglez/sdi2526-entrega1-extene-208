package com.uniovi.sdi2526entrega121.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;


/**
 * Representa un trayecto realizado con un vehículo, incluyendo información como
 * la hora de inicio y fin, valores del odómetro, observaciones, el vehículo utilizado
 * y el empleado que lo condujo.
 */

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "odometer_init_value")
    private Double odometerInitValue;

    @Column(name = "odometer_final_value")
    private Double odometerFinalValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 1000)
    private String observations;

    public Route() {
    }

    public Route(Timestamp startTime, Timestamp endTime, Double odometerInitValue,
                 Double odometerFinalValue, Vehicle vehicle, Employee employee, String observations) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.odometerInitValue = odometerInitValue;
        this.odometerFinalValue = odometerFinalValue;
        this.vehicle = vehicle;
        this.employee = employee;
        this.observations = observations;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Double getOdometerInitValue() {
        return odometerInitValue;
    }

    public void setOdometerInitValue(Double odometerInitValue) {
        this.odometerInitValue = odometerInitValue;
    }

    public Double getOdometerFinalValue() {
        return odometerFinalValue;
    }

    public void setOdometerFinalValue(Double odometerFinalValue) {
        this.odometerFinalValue = odometerFinalValue;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    // --- Logic ---

    public Duration getDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime.toInstant(), endTime.toInstant());
        }
        return Duration.ZERO;
    }


    public String getFormattedDuration() {
        Duration duration = getDuration();

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }



    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", odometerInitValue=" + odometerInitValue +
                ", odometerFinalValue=" + odometerFinalValue +
                ", vehicle=" + (vehicle != null ? vehicle.getId() : null) +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", observations='" + observations + '\'' +
                '}';
    }
}
