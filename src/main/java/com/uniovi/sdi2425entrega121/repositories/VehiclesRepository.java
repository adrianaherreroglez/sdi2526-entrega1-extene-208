package com.uniovi.sdi2425entrega121.repositories;

import com.uniovi.sdi2425entrega121.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Repositorio para la entidad {@link Vehicle}, que extiende {@link CrudRepository}
 * para proporcionar operaciones CRUD básicas y consultas específicas sobre vehículos.
 */
public interface VehiclesRepository extends CrudRepository<Vehicle, Long>{

    Page<Vehicle> findAll(Pageable pageable);

    Vehicle findByPlate(String plate);

    Vehicle findByChassisNumber(String chassisNumber);

    Page<Vehicle> findByStatus(Vehicle.Status status, Pageable pageable);

    @Query("SELECT COUNT(v) FROM Vehicle v")
    int countAll();

    @Modifying
    @Transactional
    @Query("DELETE FROM Vehicle v WHERE v.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);
}
