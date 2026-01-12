package com.uniovi.sdi2526entrega121.controllers;

import com.uniovi.sdi2526entrega121.entities.Vehicle;
import com.uniovi.sdi2526entrega121.services.VehiclesService;
import com.uniovi.sdi2526entrega121.validators.VehiclesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con vehículos,
 * tales como listado, creación, eliminación y visualización de detalles.
 */
@Controller
public class VehiclesController {

    private final VehiclesService vehiclesService;
    private final VehiclesValidator vehiclesValidator;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param vehiclesService Servicio para operaciones sobre vehículos.
     * @param vehiclesValidator Validador para los datos de vehículos.
     */
    @Autowired
    public VehiclesController(VehiclesService vehiclesService, VehiclesValidator vehiclesValidator) {
        this.vehiclesService = vehiclesService;
        this.vehiclesValidator = vehiclesValidator;
    }

    /**
     * Muestra la lista paginada de vehículos.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param pageable Información de paginación.
     * @return Vista con la lista de vehículos.
     */
    @RequestMapping("/vehicle/list")
    public String getList(Model model, Pageable pageable) {
        Page<Vehicle> page = vehiclesService.getVehicles(pageable);
        model.addAttribute("page", page);
        return "vehicle/list";
    }

    /**
     * Procesa el formulario de alta de un nuevo vehículo, validando sus datos.
     * Si hay errores, devuelve el formulario con mensajes; si no, guarda el vehículo.
     *
     * @param vehicle Vehículo a crear.
     * @param result Resultado de la validación.
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la lista o recarga del formulario en caso de error.
     */
    @RequestMapping(value = "vehicle/add", method = RequestMethod.POST)
    public String setVehicle(@Validated Vehicle vehicle, BindingResult result, Model model) {
        vehiclesValidator.validate(vehicle, result);
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("fuelTypes", Vehicle.FuelType.values());
            return "vehicle/add";
        }
        vehiclesService.addVehicle(vehicle);
        return "redirect:/vehicle/list";
    }

    /**
     * Muestra el formulario para crear un nuevo vehículo.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista con el formulario para añadir un vehículo.
     */
    @RequestMapping(value = "/vehicle/add")
    public String getVehicle(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypes", Vehicle.FuelType.values());
        return "vehicle/add";
    }

    /**
     * Elimina múltiples vehículos a partir de una lista de IDs.
     * Responde con un cuerpo HTTP en formato String (nombre vista).
     *
     * @param vehicleIds Lista de IDs de vehículos a eliminar.
     * @return Nombre de la vista para redirección o actualización.
     */
    @RequestMapping(value = "/vehicle/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMultipleVehicles(@RequestBody List<Long> vehicleIds) {
        vehiclesService.deleteVehicles(vehicleIds);
        return "vehicle/list";
    }

    /**
     * Muestra la lista paginada de vehículos disponibles.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param pageable Información de paginación.
     * @return Vista con la lista de vehículos disponibles.
     */
    @RequestMapping("/vehicle/list/available")
    public String getAvailableVehicles(Model model, Pageable pageable) {
        Page<Vehicle> page = vehiclesService.getAvailableVehicles(pageable);
        model.addAttribute("page", page);
        return "vehicle/listAvailable";
    }

    /**
     * Muestra las rutas asociadas a un vehículo dado.
     *
     * @param id ID del vehículo.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista con las rutas del vehículo.
     */
    @RequestMapping("/vehicle/routes/{id}")
    public String getVehicleRoutes(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehiclesService.getVehicle(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("routes", vehicle.getRoutes());
        return "vehicle/routes";
    }

    /**
     * Muestra el listado de repostajes asociados a un vehículo.
     * Redirige a la vista de repostajes filtrada por la matrícula del vehículo.
     *
     * @param id ID del vehículo.
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la lista de repostajes para el vehículo.
     */
    @RequestMapping("/vehicle/refuels/{id}")
    public String getVehicleRefuels(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehiclesService.getVehicle(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("refuels", vehicle.getRefuels());
        return "redirect:/refuel/list/" + vehicle.getPlate();
    }

    /**
     * Muestra un listado paginado de matrículas de vehículos.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param page Número de página (por defecto 0).
     * @return Vista con la lista de matrículas.
     */
    @GetMapping("/vehicle/list/plates")
    public String listPlates(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Vehicle> vehiclesPage = vehiclesService.getVehicles(pageable);
        model.addAttribute("page", vehiclesPage);
        return "vehicle/listPlates";
    }

}
