package com.uniovi.sdi2526entrega121.controllers;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.Route;
import com.uniovi.sdi2526entrega121.entities.Vehicle;
import com.uniovi.sdi2526entrega121.services.EmployeesService;
import com.uniovi.sdi2526entrega121.services.RoutesService;
import com.uniovi.sdi2526entrega121.services.VehiclesService;
import com.uniovi.sdi2526entrega121.validators.AddRouteValidator;
import com.uniovi.sdi2526entrega121.validators.EndRouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Controlador para gestionar las rutas de los empleados y vehículos.
 */
@Controller
public class RouteController {

    private final RoutesService routesService;
    private final EmployeesService employeesService;
    private final VehiclesService vehiclesService;
    private final AddRouteValidator addRouteValidator;
    private final EndRouteValidator endRouteValidator;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param routesService Servicio para gestión de rutas.
     * @param employeesService Servicio para gestión de empleados.
     * @param vehiclesService Servicio para gestión de vehículos.
     * @param addRouteValidator Validador para la creación de rutas.
     * @param endRouteValidator Validador para el fin de rutas.
     */
    @Autowired
    public RouteController(RoutesService routesService, EmployeesService employeesService,
                           VehiclesService vehiclesService, AddRouteValidator addRouteValidator,
                           EndRouteValidator endRouteValidator) {
        this.routesService = routesService;
        this.employeesService = employeesService;
        this.vehiclesService = vehiclesService;
        this.addRouteValidator = addRouteValidator;
        this.endRouteValidator = endRouteValidator;
    }

    /**
     * Muestra la lista paginada de rutas asociadas al empleado autenticado.
     * También indica la ruta activa, si existe.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param principal Información del usuario autenticado.
     * @param pageable Parámetros de paginación.
     * @return Vista con la lista de rutas.
     */
    @RequestMapping("/routes/list")
    public String getList(Model model, Principal principal, Pageable pageable) {
        String dni = principal.getName();
        Employee authEmployee = employeesService.getEmployeeByDni(dni);
        Page<Route> routes = routesService.getAllRoutesForEmployee(authEmployee, pageable);
        Route currentRoute = routesService.getActiveRouteByEmployeeDni(authEmployee.getDni());

        model.addAttribute("routes", routes.getContent());
        model.addAttribute("page", routes);
        if (currentRoute != null) {
            model.addAttribute("routeId", currentRoute.getId());
        }
        return "routes/list";
    }

    /**
     * Muestra el formulario para añadir una nueva ruta.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de añadir ruta.
     */
    @RequestMapping("/routes/add")
    public String setRoute(Model model) {
        Pageable pageable = Pageable.unpaged();
        List<Vehicle> vehicleList = vehiclesService.getVehicles(pageable).getContent();
        model.addAttribute("vehicles", vehicleList);
        model.addAttribute("route", new Route());
        return "routes/add";
    }

    /**
     * Procesa el formulario para añadir una nueva ruta.
     *
     * @param matricula Matrícula del vehículo seleccionado.
     * @param route Objeto Route validado con los datos del formulario.
     * @param result Resultado de la validación.
     * @param principal Información del usuario autenticado.
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la lista de rutas o recarga del formulario si hay errores.
     */
    @RequestMapping(value = "/routes/add", method = RequestMethod.POST)
    public String setRoute(@RequestParam("matricula") String matricula, @Validated Route route,
                           BindingResult result, Principal principal, Model model) {
        Vehicle vehicle = vehiclesService.getVehicleByPlate(matricula);
        route.setVehicle(vehicle);

        addRouteValidator.validate(route, result);
        if (result.hasErrors()) {
            Pageable pageable = Pageable.unpaged();
            model.addAttribute("vehicles", vehiclesService.getVehicles(pageable).getContent());
            return "routes/add";
        }

        double odometerStart = vehicle.getTotalKilometers();

        route.setOdometerInitValue(odometerStart);
        route.setStartTime(new Timestamp(System.currentTimeMillis()));

        Employee employee = employeesService.getEmployeeByDni(principal.getName());
        route.setEmployee(employee);

        routesService.startRoute(route, employee, odometerStart);

        return "redirect:/routes/list";
    }

    /**
     * Muestra el formulario para finalizar la ruta activa del empleado autenticado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param principal Información del usuario autenticado.
     * @return Vista para finalizar la ruta o redirección a lista si no hay ruta activa.
     */
    @RequestMapping("/routes/end")
    public String getRouteEnd(Model model, Principal principal) {
        String dni = principal.getName();
        Route route = routesService.getActiveRouteByEmployeeDni(dni);
        if (route == null) {
            return "redirect:/routes/list";
        }
        model.addAttribute("route", route);
        return "/routes/end";
    }

    /**
     * Procesa el formulario para finalizar la ruta activa.
     *
     * @param formRoute Objeto Route con los datos enviados para finalizar la ruta.
     * @param result Resultado de la validación.
     * @param principal Información del usuario autenticado.
     * @return Redirección a la vista de rutas del vehículo asociado.
     */
    @RequestMapping(value = "/routes/end", method = RequestMethod.POST)
    public String setRouteEnd(@ModelAttribute @Validated Route formRoute, BindingResult result, Principal principal) {
        Route route = routesService.getActiveRouteByEmployeeDni(principal.getName());
        route.setOdometerFinalValue(formRoute.getOdometerFinalValue());

        endRouteValidator.validate(route, result);
        if (result.hasErrors()) {
            return "/routes/end";
        }
        routesService.endRoute(route);

        route.getVehicle().setStatus(Vehicle.Status.LIBRE);
        vehiclesService.updateVehicle(route.getVehicle());

        Long id = route.getVehicle().getId();
        return "redirect:/vehicle/routes/" + id;
    }
}
