package com.uniovi.sdi2526entrega121.services;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.User;
import com.uniovi.sdi2526entrega121.repositories.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Servicio para la gestión de empleados, que encapsula la lógica de negocio
 * y operaciones sobre la entidad {@link Employee}.
 */
@Service
public class EmployeesService {

  private final EmployeesRepository employeesRepository;
  private final UsersService usersService;

  /**
   * Constructor con inyección de dependencias de los repositorios y servicios necesarios.
   *
   * @param employeesRepository repositorio para operaciones sobre empleados
   * @param usersService servicio para operaciones relacionadas con usuarios
   */
  @Autowired
  public EmployeesService(EmployeesRepository employeesRepository, UsersService usersService) {
    this.employeesRepository = employeesRepository;
    this.usersService = usersService;
  }

  /**
   * Obtiene una página de empleados paginada.
   *
   * @param pageable información de paginación
   * @return página con los empleados
   */
  public Page<Employee> getEmployees(Pageable pageable) {
    return employeesRepository.findAll(pageable);
  }

  /**
   * Busca un empleado por su id.
   *
   * @param id identificador del empleado
   * @return {@link Optional} que contiene el empleado si existe, o vacío si no
   */
  public Optional<Employee> getEmployee(Long id) {
    return employeesRepository.findById(id);
  }

  /**
   * Añade un nuevo empleado a la base de datos.
   *
   * @param employee empleado a guardar
   */
  public void addEmployee(Employee employee) {
    employeesRepository.save(employee);
  }

  /**
   * Obtiene un empleado mediante su DNI.
   *
   * @param dni DNI del empleado
   * @return empleado correspondiente al DNI, o {@code null} si no existe
   */
  public Employee getEmployeeByDni(String dni) {
    return employeesRepository.findByDni(dni);
  }

  /**
   * Devuelve el número total de empleados en la base de datos.
   *
   * @return número total de empleados
   */
  public int getNumberOfEmployees() {
    return employeesRepository.countAll();
  }

  /**
   * Devuelve el rol asignado a un empleado concreto.
   *
   * @param id identificador del empleado
   * @return rol del empleado, o {@code null} si no existe
   */
  public String getEmployeeRole(Long id) {
    Optional<Employee> emp = getEmployee(id);
    if (emp.isEmpty()) {
      return null;
    }
    Employee employee = emp.get();
    return usersService.getUserByDni(employee.getDni()).getRole();
  }

  /**
   * Edita los datos de un empleado y su rol.
   *
   * @param id identificador del empleado a modificar
   * @param employee objeto con la información actualizada del empleado
   * @param role nuevo rol que se asignará al empleado
   * @throws IllegalArgumentException si no se encuentra el empleado con el id dado
   */
  public void editEmployee(long id, Employee employee, String role) {
    Optional<Employee> optOriginalEmployee = getEmployee(id);
    if (optOriginalEmployee.isEmpty()) {
      throw new IllegalArgumentException("No employee found with id " + id);
    }
    Employee originalEmployee = optOriginalEmployee.get();

    // Modificar datos del empleado
    originalEmployee.setDni(employee.getDni());
    originalEmployee.setName(employee.getName());
    originalEmployee.setSurname(employee.getSurname());
    employeesRepository.save(originalEmployee);

    // Actualizar datos del usuario relacionado
    User user = originalEmployee.getUser();
    usersService.editUserFromEmployee(user.getId(), employee, role);
  }
}