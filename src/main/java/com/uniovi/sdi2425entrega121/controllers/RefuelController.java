package com.uniovi.sdi2425entrega121.controllers;

import com.uniovi.sdi2425entrega121.entities.Refuel;
import com.uniovi.sdi2425entrega121.services.RefuelService;
import com.uniovi.sdi2425entrega121.services.VehiclesService;
import com.uniovi.sdi2425entrega121.validators.AddRefuelFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.security.Principal;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los repostajes.
 */
@Controller
public class RefuelController {

    private final RefuelService refuelService;
    private final VehiclesService vehiclesService;
    private final AddRefuelFormValidator addRefuelFormValidator;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param refuelService Servicio que gestiona los repostajes.
     * @param vehiclesService Servicio que gestiona los vehículos.
     * @param addRefuelFormValidator Validador para el formulario de añadir repostajes.
     */
    @Autowired
    public RefuelController(RefuelService refuelService, VehiclesService vehiclesService,
                            AddRefuelFormValidator addRefuelFormValidator) {
        this.refuelService = refuelService;
        this.vehiclesService = vehiclesService;
        this.addRefuelFormValidator = addRefuelFormValidator;
    }

    /**
     * Muestra la lista paginada de repostajes de un vehículo identificado por su matrícula.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param plate Matrícula del vehículo.
     * @param pageable Información de paginación.
     * @return Vista con la lista de repostajes para el vehículo.
     */
    @RequestMapping("/refuel/list/{plate}")
    public String getList(Model model, @PathVariable String plate, Pageable pageable){
        Page<Refuel> refuels = refuelService.getRefuelsForVehicle(plate, pageable);
        model.addAttribute("refuelsList", refuels.getContent());
        model.addAttribute("page", refuels);
        model.addAttribute("plate", plate);
        return "refuel/list";
    }

    /**
     * Procesa el formulario para añadir un nuevo repostaje.
     *
     * @param refuel Objeto Refuel validado que contiene los datos del repostaje.
     * @param result Resultado de la validación.
     * @param model Modelo para pasar datos a la vista.
     * @param principal Información del usuario autenticado.
     * @param page Número de página actual para la paginación.
     * @return Redirección a la lista de repostajes o recarga del formulario si hay errores.
     */
    @RequestMapping(value="/refuel/add", method = RequestMethod.POST)
    public String setRefuel(@Validated Refuel refuel, BindingResult result, Model model,
                            Principal principal, @RequestParam(value="page", defaultValue = "0") int page) {

        addRefuelFormValidator.validate(refuel, result);

        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(page, 5);
            model.addAttribute("vehiclesList", vehiclesService.getVehicles(pageable));
            return "refuel/add";
        }

        String dni = principal.getName();
        refuelService.addVehicle(refuel, dni);
        refuelService.setRefuelDate(refuel);
        refuelService.calculateTotalPrice(refuel);
        refuelService.addRefuel(refuel);

        return "redirect:/refuel/list/" + refuel.getVehicle().getPlate();
    }

    /**
     * Muestra el formulario para añadir un repostaje.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param page Número de página para la lista de vehículos paginada.
     * @return Vista del formulario para añadir repostaje.
     */
    @RequestMapping(value="/refuel/add")
    public String getRefuel(Model model, @RequestParam(value="page", defaultValue="0") int page){
        Pageable pageable = PageRequest.of(page, 5);
        model.addAttribute("refuel", new Refuel());
        model.addAttribute("vehiclesList", vehiclesService.getVehicles(pageable));
        return "refuel/add";
    }
}
