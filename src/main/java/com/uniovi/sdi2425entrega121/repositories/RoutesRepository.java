package com.uniovi.sdi2425entrega121.repositories;


import com.uniovi.sdi2425entrega121.entities.Employee;
import com.uniovi.sdi2425entrega121.entities.Route;
import com.uniovi.sdi2425entrega121.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Route}, que proporciona operaciones CRUD
 * y consultas específicas relacionadas con rutas, vehículos y empleados.
 * Extiende {@link CrudRepository} para operaciones básicas de persistencia.
 */
public interface RoutesRepository extends CrudRepository<Route, Long> {
    @Query("SELECT r FROM Route r WHERE r.employee = ?1 ORDER BY r.id ASC")
    Page<Route> findByEmployee(Employee employee, Pageable pageable);

    @Query("SELECT r FROM Route r WHERE r.employee.dni= ?1 AND r.endTime IS NULL")
    Route findActiveByDni(String dni);

    int countByVehicle(Vehicle vehicle);

    int countByEmployee(Employee employee);


}
