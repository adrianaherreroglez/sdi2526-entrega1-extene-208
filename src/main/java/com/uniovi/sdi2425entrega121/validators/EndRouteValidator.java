package com.uniovi.sdi2425entrega121.validators;

import com.uniovi.sdi2425entrega121.entities.Route;
import com.uniovi.sdi2425entrega121.services.EmployeesService;
import com.uniovi.sdi2425entrega121.services.RoutesService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EndRouteValidator implements Validator {

    // Puedes conservar las dependencias si en el futuro necesitas validaciones más complejas
    private final EmployeesService employeesService;
    private final RoutesService routesService;
    private final MessageSource messageSource;

    public EndRouteValidator(EmployeesService employeesService,
                             RoutesService routesService,
                             MessageSource messageSource) {
        this.employeesService = employeesService;
        this.routesService = routesService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Route.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Route route = (Route) target;

        // Validación: el campo no puede estar vacío
        if (route.getOdometerFinalValue() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "odometerFinalValue", "Error.empty");
            return;
        }

        // Validación: el valor final del odómetro debe ser mayor que 0
        if (route.getOdometerFinalValue() < 0) {
            errors.rejectValue("odometerFinalValue", "error.route.end.negative");
            return;
        }



        // Validación: el valor final del odómetro debe ser mayor o igual al valor inicial
        if (route.getOdometerFinalValue() < route.getOdometerInitValue()) {
            errors.rejectValue("odometerFinalValue", "error.route.end.odometerfinalvalue");
        }
    }
}
