package com.uniovi.sdi2425entrega121.validators;

import com.uniovi.sdi2425entrega121.entities.Employee;
import com.uniovi.sdi2425entrega121.entities.Refuel;
import com.uniovi.sdi2425entrega121.entities.Route;
import com.uniovi.sdi2425entrega121.entities.User;
import com.uniovi.sdi2425entrega121.services.EmployeesService;
import com.uniovi.sdi2425entrega121.services.RefuelService;
import com.uniovi.sdi2425entrega121.services.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Locale;

@Component
public class AddRefuelFormValidator implements Validator {

    private final RefuelService refuelService;
    private final RoutesService routeService;
    private final MessageSource messageSource;

    public AddRefuelFormValidator(RefuelService refuelService, RoutesService routeService,
                                                                        MessageSource messageSource) {
        this.refuelService = refuelService;
        this.routeService = routeService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Refuel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refuel refuel = (Refuel) target;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Employee employee = refuelService.getEmployeeByDni(user.getUsername());
        Route route = routeService.getActiveRouteByEmployeeDni(employee.getDni());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "stationName", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pricePerUnit", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "totalPrice", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "odometerRefuelValue", "Error.empty");

        if (route == null) {
            String error = messageSource.getMessage("Error.refuel.employee.route.notActive", null, Locale.getDefault());
            errors.reject("error.refuelForm", error);
        } else if (refuel.getOdometerRefuelValue() < route.getOdometerInitValue()) {
            String error = messageSource.getMessage("Error.refuel.odometer.higherOnStart", null, Locale.getDefault());
            errors.rejectValue("odometerRefuelValue", "Error.refuel.odometer.higherOnStart", error);
        }




        if(refuel.getStationName().startsWith(" ") || refuel.getStationName().endsWith(" ")){
            errors.rejectValue("stationName", "Error.refuel.whitespace");
        }

        if(refuel.getPricePerUnit() < 0){
            errors.rejectValue("pricePerUnit", "Error.refuel.pricePerUnit.negative");
        }



        if(refuel.getQuantity() < 0) {
            errors.rejectValue("quantity", "Error.refuel.quantity.negative");
        }

        if(refuel.getOdometerRefuelValue() < 0){
            errors.rejectValue("odometerRefuelValue", "Error.refuel.odometer.higherOnStart");
        }
    }
}
