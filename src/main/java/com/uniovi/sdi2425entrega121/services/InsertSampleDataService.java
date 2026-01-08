package com.uniovi.sdi2425entrega121.services;

import com.uniovi.sdi2425entrega121.entities.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio que introduce los datos de prueba utilizados en los tests a la base de datos
 */
@Service
public class InsertSampleDataService {

  private final UsersService usersService;
  private final RolesService rolesService;
  private final EmployeesService employeeService;
  private final VehiclesService vehiclesService;
  private final RoutesService routesService;
  private final RefuelService refuelService;

  public InsertSampleDataService(UsersService usersService, RolesService rolesService,
                                 EmployeesService employeeService, VehiclesService vehiclesService,
                                 RoutesService routesService, RefuelService refuelService) {
    this.usersService = usersService;
    this.rolesService = rolesService;
    this.employeeService = employeeService;
    this.vehiclesService = vehiclesService;
    this.routesService = routesService;
    this.refuelService = refuelService;
  }

  @PostConstruct
  public void init() {
    // Empleado 0 (ADMINISTRADOR)
    Employee employee0 = new Employee("12345678Z", "Lucas", "Sámchez");
    employeeService.addEmployee(employee0);
    User user0 = new User("12345678Z", employee0);
    user0.setPassword("@Dm1n1str@D0r");
    user0.setRole(rolesService.getRoles()[1]);

    // Empleado 1
    Employee employee1 = new Employee("10000001S", "Diego", "Sámchez");
    employeeService.addEmployee(employee1);
    User user1 = new User("10000001S", employee1);
    user1.setPassword("Us3r@1-PASSW");
    user1.setRole(rolesService.getRoles()[0]);

    // Empleado 2
    Employee employee2 = new Employee("10000001B", "Laura", "González");
    employeeService.addEmployee(employee2);
    User user2 = new User("10000001B", employee2);
    user2.setPassword("Us3r@2-PASSW");
    user2.setRole(rolesService.getRoles()[0]);

    // Empleado 3
    Employee employee3 = new Employee("10000001C", "Ana", "Moreno");
    employeeService.addEmployee(employee3);
    User user3 = new User("10000001C", employee3);
    user3.setPassword("Us3r@3-PASSW");
    user3.setRole(rolesService.getRoles()[0]);

    // Empleado 4
    Employee employee4 = new Employee("10000001D", "Paula", "López");
    employeeService.addEmployee(employee4);
    User user4 = new User("10000001D", employee4);
    user4.setPassword("Us3r@4-PASSW");
    user4.setRole(rolesService.getRoles()[0]); // Asignando un rol diferente

    // Empleado 5
    Employee employee5 = new Employee("10000001E", "María", "Ruiz");
    employeeService.addEmployee(employee5);
    User user5 = new User("10000001E", employee5);
    user5.setPassword("Us3r@5-PASSW");
    user5.setRole(rolesService.getRoles()[0]);

    // Empleado 6
    Employee employee6 = new Employee("10000001F", "Carlos", "Hernández");
    employeeService.addEmployee(employee6);
    User user6 = new User("10000001F", employee6);
    user6.setPassword("Us3r@6-PASSW");
    user6.setRole(rolesService.getRoles()[0]);

    // Empleado 7
    Employee employee7 = new Employee("10000001G", "Sandra", "García");
    employeeService.addEmployee(employee7);
    User user7 = new User("10000001G", employee7);
    user7.setPassword("Us3r@7-PASSW");
    user7.setRole(rolesService.getRoles()[0]);

    // Empleado 8
    Employee employee8 = new Employee("10000001H", "José", "Sánchez");
    employeeService.addEmployee(employee8);
    User user8 = new User("10000001H", employee8);
    user8.setPassword("Us3r@8-PASSW");
    user8.setRole(rolesService.getRoles()[0]);

    // Empleado 9
    Employee employee9 = new Employee("10000001I", "Sofía", "Jiménez");
    employeeService.addEmployee(employee9);
    User user9 = new User("10000001I", employee9);
    user9.setPassword("Us3r@9-PASSW");
    user9.setRole(rolesService.getRoles()[0]);

    //Empleado 10
    Employee employee10 = new Employee("10000001J", "Paula", "Sámchez");
    employeeService.addEmployee(employee10);
    User user10 = new User("10000001J", employee10);
    user10.setPassword("Us3r@10-PASSW");
    user10.setRole(rolesService.getRoles()[0]);

    //Empleado 11
    Employee employee11 = new Employee("10000001K", "Pepe", "Sámchez");
    employeeService.addEmployee(employee11);
    User user11 = new User("10000001K", employee11);
    user11.setPassword("Us3r@11-PASSW");
    user11.setRole(rolesService.getRoles()[0]);

    //Empleado 12
    Employee employee12 = new Employee("10000001L", "Test", "User");
    employeeService.addEmployee(employee12);
    User user12 = new User("10000001L", employee12);
    user12.setPassword("Us3r@12-PASSW");
    user12.setRole(rolesService.getRoles()[0]);

    //Empleado 13
    Employee employee13 = new Employee("10000001M", "Paula", "Jose");
    employeeService.addEmployee(employee13);
    User user13 = new User("10000001M", employee13);
    user13.setPassword("Us3r@13-PASSW");
    user13.setRole(rolesService.getRoles()[0]);

    //Empleado 14
    Employee employee14 = new Employee("10000001N", "Sara", "Vega");
    employeeService.addEmployee(employee14);
    User user14 = new User("10000001N", employee14);
    user14.setPassword("Us3r@14-PASSW");
    user14.setRole(rolesService.getRoles()[0]);

    //Empleado 15, sin trayectos
    Employee employee15 = new Employee("10000001P", "Sara", "Vega");
    employeeService.addEmployee(employee15);
    User user15 = new User("10000001P", employee15);
    user15.setPassword("Us3r@15-PASSW");
    user15.setRole(rolesService.getRoles()[0]);


    // Añadir usuarios al sistema
    usersService.addUser(user0);
    usersService.addUser(user1);
    usersService.addUser(user2);
    usersService.addUser(user3);
    usersService.addUser(user4);
    usersService.addUser(user5);
    usersService.addUser(user6);
    usersService.addUser(user7);
    usersService.addUser(user9);
    usersService.addUser(user10);
    usersService.addUser(user11);
    usersService.addUser(user12);
    usersService.addUser(user13);
    usersService.addUser(user14);
    usersService.addUser(user15);

    List<Employee> employees = new ArrayList<>();
    employees.add(employee1);
    employees.add(employee2);
    employees.add(employee3);
    employees.add(employee4);
    employees.add(employee5);
    employees.add(employee6);
    employees.add(employee7);
    employees.add(employee8);
    employees.add(employee9);
    employees.add(employee10);
    employees.add(employee11);
    employees.add(employee12);
    employees.add(employee13);
    employees.add(employee14);
    employees.add(employee15);

    // Añadir 10 vehículos al sistema
    Vehicle v1 = new Vehicle("1234BCF", "Corolla", "Toyota", "1HGCM82633A123456", Vehicle.FuelType.GASOLINA);
    Vehicle v2 = new Vehicle("5678DFG", "Civic", "Honda", "2HGCM82633A654321", Vehicle.FuelType.DIESEL);
    Vehicle v3 = new Vehicle("9101GHJ", "Model 3", "Tesla", "3HGCM82633A789012", Vehicle.FuelType.ELECTRICO);
    Vehicle v4 = new Vehicle("2345JKL", "A4", "Audi", "4HGCM82633A345678", Vehicle.FuelType.HIBRIDO);
    Vehicle v5 = new Vehicle("6789LMN", "Serie 3", "BMW", "5HGCM82633A901234", Vehicle.FuelType.MICROHIBRIDO);
    Vehicle v6 = new Vehicle("3456NPR", "Focus", "Ford", "6HGCM82633A567890", Vehicle.FuelType.GLP);
    Vehicle v7 = new Vehicle("7890RST", "Corsa", "Opel", "7HGCM82633A234567", Vehicle.FuelType.GNL);
    Vehicle v8 =
            new Vehicle("4567TVW", "Ibiza", "Seat", "8HGCM82633A678901", Vehicle.FuelType.GASOLINA);
    Vehicle v9 =
            new Vehicle("8901XYZ", "Clio", "Renault", "9HGCM82633A345678", Vehicle.FuelType.DIESEL);
    Vehicle v10 =
            new Vehicle("0123BCF", "Golf", "Volkswagen", "0HGCM82633A901234", Vehicle.FuelType.HIBRIDO);
    Vehicle v11 =
            new Vehicle("0123BCA", "Golf", "Volkswagen", "0HGCM82633A901235", Vehicle.FuelType.HIBRIDO);
    Vehicle v12 =
            new Vehicle("0123BCB", "Golf", "Volkswagen", "0HGCM82633A901236", Vehicle.FuelType.HIBRIDO);
    Vehicle v13 =
            new Vehicle("0123BCC", "Golf", "Volkswagen", "0HGCM82633A901237", Vehicle.FuelType.HIBRIDO);
    Vehicle v14 =
            new Vehicle("0123BCD", "Golf", "Volkswagen", "0HGCM82633A901238", Vehicle.FuelType.HIBRIDO);
    Vehicle v15 =
            new Vehicle("0123BCE", "Golf", "Volkswagen", "0HGCM82633A901239", Vehicle.FuelType.HIBRIDO);

    vehiclesService.addVehicle(v1);
    vehiclesService.addVehicle(v2);
    vehiclesService.addVehicle(v3);
    vehiclesService.addVehicle(v4);
    vehiclesService.addVehicle(v5);
    vehiclesService.addVehicle(v6);
    vehiclesService.addVehicle(v7);
    vehiclesService.addVehicle(v8);
    vehiclesService.addVehicle(v9);
    vehiclesService.addVehicle(v10);
    vehiclesService.addVehicle(v11);
    vehiclesService.addVehicle(v12);
    vehiclesService.addVehicle(v13);
    vehiclesService.addVehicle(v14);
    vehiclesService.addVehicle(v15);

    // Bucle para hacer que cada empleado tenga 10 rutas
    for (Employee e : employees) {
      for (int i = 0; i < 10; i++) {
        Route ruta = new Route(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis() + 1000 * i), i * 100.0, i * 200.0, v15, e,
                "sin observaciones");
        routesService.saveRoute(ruta);
      }
    }

    Route ruta1 =
            new Route(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis() + 1600), 0.0, 234890.0, v7, null,
                    "sin observaciones");
    Route ruta2 =
            new Route(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis() + 2000), 0.0, null, v1, null,
                    "sin observaciones.");
    Route ruta3 =
            new Route(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis() + 300), 0.0, null, v2,
                    null, "sin observaciones.");
    Route ruta4 =
            new Route(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis() + 670), 0.0, null, v3,
                    null, "sin observaciones.");

    routesService.startRoute(ruta1, employee11, 100);
    routesService.startRoute(ruta2, employee12, 100);
    routesService.startRoute(ruta3, employee13, 100);
    routesService.startRoute(ruta4, employee14, 100);

    Refuel r1 = new Refuel("Estación1", 12.3, 4, true, 5000, "");
    r1.setVehicle(v1);
    refuelService.setRefuelDate(r1);
    refuelService.calculateTotalPrice(r1);
    refuelService.addRefuel(r1);

    Refuel r2 = new Refuel("Estación2", 15.3, 532, false, 6000, "clerk was a jerk");
    r2.setVehicle(v1);
    refuelService.setRefuelDate(r2);
    refuelService.calculateTotalPrice(r2);
    refuelService.addRefuel(r2);

    Refuel r3 = new Refuel("Estación3", 54.3, 12, true, 7000, "");
    r3.setVehicle(v1);
    refuelService.setRefuelDate(r3);
    refuelService.calculateTotalPrice(r3);
    refuelService.addRefuel(r3);

    Refuel r4 = new Refuel("Estación4", 129.3, 9, false, 8000, "horrible bagels");
    r4.setVehicle(v1);
    refuelService.setRefuelDate(r4);
    refuelService.calculateTotalPrice(r4);
    refuelService.addRefuel(r4);

    Refuel r5 = new Refuel("Estación5", 6.53, 2, false, 8000, "quick refill");
    r5.setVehicle(v1);
    refuelService.setRefuelDate(r5);
    refuelService.calculateTotalPrice(r5);
    refuelService.addRefuel(r5);

    Refuel r6 = new Refuel("Estación6", 69.3, 98, false, 8000, "road trip fuel");
    r6.setVehicle(v1);
    refuelService.setRefuelDate(r6);
    refuelService.calculateTotalPrice(r6);
    refuelService.addRefuel(r6);

    Refuel r7 = new Refuel("Estación7", 64.32, 12, false, 8000, "bad coffee");
    r7.setVehicle(v1);
    refuelService.setRefuelDate(r7);
    refuelService.calculateTotalPrice(r7);
    refuelService.addRefuel(r7);

    Refuel r8 = new Refuel("Estación8", 18.5, 52, false, 8000, "nice attendants");
    r8.setVehicle(v1);
    refuelService.setRefuelDate(r8);
    refuelService.calculateTotalPrice(r8);
    refuelService.addRefuel(r8);

    Refuel r9 = new Refuel("Estación9", 76.2, 62, false, 8000, "slow service");
    r9.setVehicle(v1);
    refuelService.setRefuelDate(r9);
    refuelService.calculateTotalPrice(r9);
    refuelService.addRefuel(r9);

    Refuel r10 = new Refuel("Estación10", 41.3, 82, false, 8000, "empty pumps");
    r10.setVehicle(v1);
    refuelService.setRefuelDate(r10);
    refuelService.calculateTotalPrice(r10);
    refuelService.addRefuel(r10);
  }
}