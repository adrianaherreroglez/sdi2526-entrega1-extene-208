package com.uniovi.sdi2526entrega121.validators;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.Route;
import com.uniovi.sdi2526entrega121.services.EmployeesService;
import com.uniovi.sdi2526entrega121.services.RoutesService;
import com.uniovi.sdi2526entrega121.services.VehiclesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddRouteValidator implements Validator {

    private final EmployeesService employeesService;
    private final RoutesService routesService;
    private final VehiclesService vehiclesService;

    public AddRouteValidator(EmployeesService employeesService, RoutesService routesService,
                             VehiclesService vehiclesService) {
        this.employeesService = employeesService;
        this.routesService = routesService;
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Route.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Route route = (Route) target;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Employee employee = employeesService.getEmployeeByDni(user.getUsername());

        // No se admiten campos vacíos o en blanco. Si el trayecto no tiene vehículo es que
        // se ha obtenido un campo vacío.
        if(route.getVehicle() == null) {
            errors.rejectValue("vehicle.plate", "Error.empty");
        } else  {
            // Si el vehículo ya esta ocupado por otro empleado
            if(!vehiclesService.isVehicleAvailable(route.getVehicle())){
                errors.rejectValue("vehicle.plate", "Error.route.notFreeVehicle");
            }
        }
        //Si ya tiene un trayecto en curso, no puede iniciar uno nuevo
        if(routesService.getActiveRouteByEmployeeDni(employee.getDni()) != null) {
            errors.rejectValue("vehicle.plate", "Error.route.alreadyOnRoute");
        }

    }
}