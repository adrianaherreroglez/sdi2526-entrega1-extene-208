package com.uniovi.sdi2526entrega121.services;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.Route;
import com.uniovi.sdi2526entrega121.entities.Vehicle;
import com.uniovi.sdi2526entrega121.repositories.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

/**
 * Servicio para la gestión de rutas, incluyendo creación, consulta y actualización.
 */
@Service
public class RoutesService {

    /**
     * Repositorio para acceder a la persistencia de las rutas.
     */
    private final RoutesRepository routesRepository;

    /**
     * Constructor con inyección del repositorio para evitar warnings.
     *
     * @param routesRepository Repositorio de rutas
     */
    @Autowired
    public RoutesService(RoutesRepository routesRepository) {
        this.routesRepository = routesRepository;
    }

    /**
     * Obtiene una página de rutas asociadas a un empleado específico.
     *
     * @param employee empleado propietario de las rutas
     * @param pageable configuración de paginación
     * @return página con las rutas del empleado
     */
    public Page<Route> getAllRoutesForEmployee(Employee employee, Pageable pageable) {
        return routesRepository.findByEmployee(employee, pageable);
    }

    /**
     * Obtiene la ruta activa (sin finalizar) asociada al empleado con el DNI dado.
     *
     * @param dni DNI del empleado
     * @return ruta activa o null si no existe
     */
    public Route getActiveRouteByEmployeeDni(String dni) {
        return routesRepository.findActiveByDni(dni);
    }

    /**
     * Guarda o actualiza una ruta en la base de datos.
     *
     * @param route ruta a guardar o actualizar
     */
    public void saveRoute(Route route) {
        routesRepository.save(route);
    }

    /**
     * Inicia una ruta asignándola a un empleado, estableciendo el valor inicial del odómetro
     * y cambiando el estado del vehículo a ocupado.
     *
     * @param route ruta a iniciar
     * @param employee empleado que inicia la ruta
     * @param odometer0 valor inicial del odómetro
     */
    public void startRoute(Route route, Employee employee, double odometer0) {
        route.setOdometerInitValue(odometer0);
        route.setEmployee(employee);
        route.setEndTime(null);
        route.getVehicle().setStatus(Vehicle.Status.OCUPADO);
        routesRepository.save(route);

        // Debugging output (recomendable eliminar o reemplazar por logs)
        System.out.println(employee.getDni());
        System.out.println(route.getVehicle().getStatus());
        System.out.println(route.getVehicle().getPlate());
    }

    /**
     * Finaliza una ruta estableciendo la fecha y hora de fin a la actual y guardándola.
     *
     * @param route ruta a finalizar
     */
    public void endRoute(Route route) {
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        route.setEndTime(endTime);
        routesRepository.save(route);
    }

    /**
     * Obtiene el número total de rutas asociadas a un vehículo.
     *
     * @param vehicle vehículo del que se cuentan las rutas
     * @return número total de rutas
     */
    public int getNumberOfRoutesForVehicle(Vehicle vehicle) {
        return routesRepository.countByVehicle(vehicle);
    }

    /**
     * Obtiene el número total de rutas asociadas a un empleado.
     *
     * @param employee empleado del que se cuentan las rutas
     * @return número total de rutas
     */
    public int getNumberOfRoutesForEmployee(Employee employee) {
        return routesRepository.countByEmployee(employee);
    }
}