package com.uniovi.sdi2526entrega121.validators;

import com.uniovi.sdi2526entrega121.entities.Vehicle;
import com.uniovi.sdi2526entrega121.services.VehiclesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.regex.Pattern;


@Component
public class VehiclesValidator implements Validator {

    @Autowired
    private VehiclesService vehiclesService;

    // Expresión regular para matrículas en España (4 dígitos + 3 letras sin Ñ ni Q)
    private static final String PLATE_REGEX = "^[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}$";
    private static final Pattern PLATE_PATTERN = Pattern.compile(PLATE_REGEX);

    public static boolean isValidPlate(String plate) {
        return PLATE_PATTERN.matcher(plate).matches();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Vehicle.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vehicle vehicle = (Vehicle) target;

        // Todos los campos son obligatorios y no deben contener espacios en blanco al principio o final
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plate", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brand", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "chassisNumber", "Error.empty");

        // Validar que los valores no contengan espacios en blanco al inicio o final
        if (!vehicle.getPlate().trim().equals(vehicle.getPlate())) {
            errors.rejectValue("plate", "Error.vehicle.plate.whitespace");
        }
        if (!vehicle.getChassisNumber().trim().equals(vehicle.getChassisNumber())) {
            errors.rejectValue("chassisNumber", "Error.vehicle.chassisNumber.whitespace");
        }

        // La matrícula debe cumplir con el formato válido en España
        if (!PLATE_PATTERN.matcher(vehicle.getPlate()).matches()) {
            errors.rejectValue("plate", "Error.vehicle.plate.invalidFormat");
        }

        // La matrícula no puede estar duplicada en el sistema
        if (vehiclesService.getVehicleByPlate(vehicle.getPlate()) != null) {
            errors.rejectValue("plate", "Error.vehicle.plate.duplicate");
        }

        // El número de bastidor debe tener exactamente 17 caracteres
        if (vehicle.getChassisNumber().length() != 17) {
            errors.rejectValue("chassisNumber", "Error.vehicle.chassisNumber.length");
        }

        // El número de bastidor no puede estar repetido en el sistema
        if (vehiclesService.getVehicleByChassisNumber(vehicle.getChassisNumber()) != null) {
            errors.rejectValue("chassisNumber", "Error.vehicle.chassisNumber.duplicate");
        }
    }

}
