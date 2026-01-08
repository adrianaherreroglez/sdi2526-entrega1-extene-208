package com.uniovi.sdi2425entrega121.services;

import com.uniovi.sdi2425entrega121.entities.Vehicle;
import com.uniovi.sdi2425entrega121.repositories.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con los vehículos.
 * Proporciona métodos para obtener, crear, modificar y eliminar vehículos.
 */
@Service
public class VehiclesService {

    private final VehiclesRepository vehiclesRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param vehiclesRepository repositorio de acceso a datos de vehículos
     */
    @Autowired
    public VehiclesService(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    /**
     * Obtiene una lista paginada de todos los vehículos.
     *
     * @param pageable parámetros de paginación
     * @return página de vehículos
     */
    public Page<Vehicle> getVehicles(Pageable pageable) {
        return vehiclesRepository.findAll(pageable);
    }

    /**
     * Obtiene un vehículo por su ID.
     *
     * @param id ID del vehículo
     * @return vehículo encontrado o un nuevo objeto vacío si no existe
     */
    public Vehicle getVehicle(Long id) {
        return vehiclesRepository.findById(id).orElse(new Vehicle());
    }

    /**
     * Añade un nuevo vehículo al sistema, asignándole el estado LIBRE por defecto.
     *
     * @param vehicle vehículo a añadir
     */
    public void addVehicle(Vehicle vehicle) {
        vehicle.setStatus(Vehicle.Status.LIBRE);
        vehiclesRepository.save(vehicle);
    }

    /**
     * Busca un vehículo por su matrícula.
     *
     * @param plate matrícula del vehículo
     * @return vehículo encontrado o null si no existe
     */
    public Vehicle getVehicleByPlate(String plate) {
        return vehiclesRepository.findByPlate(plate);
    }

    /**
     * Busca un vehículo por su número de chasis.
     *
     * @param chassisNumber número de chasis
     * @return vehículo encontrado o null si no existe
     */
    public Vehicle getVehicleByChassisNumber(String chassisNumber) {
        return vehiclesRepository.findByChassisNumber(chassisNumber);
    }

    /**
     * Elimina todos los vehículos cuyos IDs se proporcionan en la lista.
     *
     * @param vehicleIds lista de IDs de vehículos a eliminar
     */
    @Transactional
    public void deleteVehicles(List<Long> vehicleIds) {
        vehiclesRepository.deleteByIds(vehicleIds);
    }

    /**
     * Obtiene una lista paginada de vehículos con estado LIBRE.
     *
     * @param pageable parámetros de paginación
     * @return página de vehículos disponibles
     */
    public Page<Vehicle> getAvailableVehicles(Pageable pageable) {
        return vehiclesRepository.findByStatus(Vehicle.Status.LIBRE, pageable);
    }

    /**
     * Cuenta el número total de vehículos registrados.
     *
     * @return número de vehículos
     */
    public int getNumberOfVehicles() {
        return vehiclesRepository.countAll();
    }

    /**
     * Actualiza el estado de un vehículo.
     *
     * @param vehicle vehículo con el nuevo estado
     * @throws java.util.NoSuchElementException si el vehículo no existe
     */
    public void updateVehicle(Vehicle vehicle) {
        Vehicle managedVehicle = vehiclesRepository.findById(vehicle.getId())
                .orElseThrow(); // lanza excepción si no se encuentra
        managedVehicle.setStatus(vehicle.getStatus());
        vehiclesRepository.save(managedVehicle);
    }

    /**
     * Verifica si un vehículo existe en el repositorio.
     *
     * @param vehicleId ID del vehículo
     * @return Optional con el vehículo si existe
     */
    public Optional<Vehicle> isVehicleInRepository(Long vehicleId) {
        return vehiclesRepository.findById(vehicleId);
    }

    /**
     * Comprueba si un vehículo está disponible (estado LIBRE).
     *
     * @param vehicle vehículo a comprobar
     * @return true si el vehículo está libre, false en caso contrario
     */
    public boolean isVehicleAvailable(Vehicle vehicle) {
        return vehicle.getStatus().equals(Vehicle.Status.LIBRE);
    }
}