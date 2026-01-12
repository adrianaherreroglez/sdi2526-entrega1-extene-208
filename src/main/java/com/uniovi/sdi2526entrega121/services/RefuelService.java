package com.uniovi.sdi2526entrega121.services;

import com.uniovi.sdi2526entrega121.entities.*;
import com.uniovi.sdi2526entrega121.repositories.RefuelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Date;



@Service
public class RefuelService {


    private final RefuelRepository refuelRepository;

    private final UsersService usersService;
    private final EmployeesService employeesService;
    private final RoutesService routeService;

    @Autowired
    public RefuelService(UsersService usersService, EmployeesService employeesService,
                         RoutesService routeService, RefuelRepository refuelRepository) {
        this.usersService = usersService;
        this.employeesService = employeesService;
        this.routeService = routeService;
        this.refuelRepository = refuelRepository;
    }

    public void addRefuel(Refuel refuel){
        refuelRepository.save(refuel);
    }

    public Page<Refuel> getRefuelsForVehicle(String plate, Pageable pageable){
        Page<Refuel> refuels;
        refuels = refuelRepository.findAllForVehicle(plate, pageable);
        return refuels;
    }

    public void calculateTotalPrice(Refuel refuel){
        refuel.setTotalPrice(refuel.getPricePerUnit() * refuel.getQuantity());
    }

    public void addVehicle(Refuel refuel, String dni){
        User user = usersService.getUserByDni(dni);
        Employee employee = employeesService.getEmployeeByDni(user.getDni());
        Route route = routeService.getActiveRouteByEmployeeDni(employee.getDni());
        Vehicle vehicle = route.getVehicle();
        refuel.setVehicle(vehicle);
    }

    public Employee getEmployeeByDni(String dni){
        return employeesService.getEmployeeByDni(dni);
    }

    public void setRefuelDate(Refuel refuel){
        refuel.setRefuelDate(new Date());
    }


}
