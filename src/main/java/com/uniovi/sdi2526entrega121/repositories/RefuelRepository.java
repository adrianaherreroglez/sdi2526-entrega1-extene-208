package com.uniovi.sdi2526entrega121.repositories;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.Refuel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad {@link Employee}, que proporciona métodos CRUD básicos
 * y consultas personalizadas para gestionar empleados.
 * Extiende {@link CrudRepository} para operaciones básicas de persistencia.
 */
public interface RefuelRepository extends CrudRepository<Refuel, Long> {

    @Query("SELECT r FROM Refuel r WHERE r.vehicle.plate = ?1 ORDER BY r.id ASC")
    Page<Refuel> findAllForVehicle(String plate, Pageable pageable);


}