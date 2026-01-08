package com.uniovi.sdi2425entrega121.controllers;

import com.uniovi.sdi2425entrega121.entities.Employee;
import com.uniovi.sdi2425entrega121.services.EmployeesService;
import com.uniovi.sdi2425entrega121.services.LoggerService;
import com.uniovi.sdi2425entrega121.services.RolesService;
import com.uniovi.sdi2425entrega121.services.UsersService;
import com.uniovi.sdi2425entrega121.validators.AddEmployeeFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controlador que gestiona las operaciones relacionadas con los empleados, tales como añadir,
 * listar, editar empleados y mostrar la contraseña generada al crear un empleado.
 */
@Controller
public class EmployeesController {

  private final AddEmployeeFormValidator addEmployeeFormValidator;
  private final EmployeesService employeesService;
  private final UsersService usersService;
  private final RolesService rolesService;
  private final LoggerService loggerService;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param addEmployeeFormValidator Validador para los formularios de creación/edición de empleados.
   * @param employeesService Servicio para la gestión de empleados.
   * @param usersService Servicio para la gestión de usuarios.
   * @param rolesService Servicio para la gestión de roles.
   * @param loggerService Servicio para el registro de logs de acciones.
   */
  @Autowired
  public EmployeesController(AddEmployeeFormValidator addEmployeeFormValidator, EmployeesService employeesService, UsersService usersService, RolesService rolesService, LoggerService loggerService) {
    this.addEmployeeFormValidator = addEmployeeFormValidator;
    this.employeesService = employeesService;
    this.usersService = usersService;
    this.rolesService = rolesService;
    this.loggerService = loggerService;
  }

  /**
   * Muestra el formulario para añadir un nuevo empleado.
   *
   * @param model Modelo para añadir atributos a la vista.
   * @return Vista del formulario para añadir empleado.
   */
  @RequestMapping(value = {"/employee/add"}, method = RequestMethod.GET)
  public String addEmployee(Model model) {
    model.addAttribute("employee", new Employee());
    return "/employee/add";
  }

  /**
   * Procesa el formulario de añadir empleado, valida los datos, crea el empleado y genera
   * un usuario asociado con contraseña, además registra la acción.
   *
   * @param employee Objeto empleado recibido del formulario.
   * @param result Resultado de la validación de los datos.
   * @param redirectAttributes Atributos para pasar datos tras redirección (contraseña).
   * @param request Solicitud HTTP para registrar la acción.
   * @return Redirección a la vista que muestra la contraseña generada, o el formulario con errores.
   */
  @RequestMapping(value = "/employee/add", method = RequestMethod.POST)
  public String addEmployee(@Validated Employee employee, BindingResult result,
                            RedirectAttributes redirectAttributes, HttpServletRequest request) {
    addEmployeeFormValidator.validate(employee, result);
    if (result.hasErrors()) {
      return "/employee/add";
    }
    employeesService.addEmployee(employee);
    String password = usersService.addUserByEmployee(employee);

    redirectAttributes.addFlashAttribute("password", password);

    loggerService.saveAddEmployeeLog(request);
    return "redirect:/employee/showPassword";
  }

  /**
   * Muestra la vista que presenta la contraseña generada al crear un empleado.
   *
   * @return Vista para mostrar la contraseña.
   */
  @RequestMapping(value = "/employee/showPassword", method = RequestMethod.GET)
  public String showPassword() {
    return "/employee/showPassword";
  }

  /**
   * Muestra la lista paginada de empleados.
   *
   * @param model Modelo para añadir atributos a la vista.
   * @param pageable Objeto que contiene la información de paginación.
   * @return Vista con la lista de empleados.
   */
  @RequestMapping(value = "/employee/list")
  public String listEmployees(Model model, Pageable pageable) {
    Page<Employee> employees = employeesService.getEmployees(pageable);
    model.addAttribute("employeesList", employees.getContent());
    model.addAttribute("page", employees);
    return "/employee/list";
  }

  /**
   * Muestra el formulario para editar un empleado existente, con sus datos actuales y roles disponibles.
   *
   * @param model Modelo para añadir atributos a la vista.
   * @param id Identificador del empleado a editar.
   * @return Vista del formulario de edición, o redirección a raíz si el empleado no existe.
   */
  @RequestMapping(value = "/employee/edit/{id}", method = RequestMethod.GET)
  public String editEmployee(Model model, @PathVariable Long id) {
    Optional<Employee> emp = employeesService.getEmployee(id);
    if(emp.isEmpty())
      return "redirect:/";
    Employee employee = emp.get();
    String[] roles = rolesService.getRoles();
    String employeeRole = employeesService.getEmployeeRole(id);

    model.addAttribute("employee", employee);
    model.addAttribute("roles", roles);
    model.addAttribute("employeeRole", employeeRole);
    return "/employee/edit";
  }

  /**
   * Procesa el formulario de edición de empleado, validando datos y actualizando la información y rol.
   *
   * @param id Identificador del empleado a editar.
   * @param employee Datos del empleado editado.
   * @param role Nuevo rol asignado al empleado.
   * @param result Resultado de la validación de datos.
   * @param model Modelo para añadir atributos a la vista en caso de error.
   * @return Redirección a la lista de empleados si se edita correctamente, o el formulario con errores.
   */
  @RequestMapping(value = "/employee/edit/{id}", method = RequestMethod.POST)
  public String editEmployee(@PathVariable long id, @Validated Employee employee,
                             @RequestParam String role, BindingResult result, Model model) {
    addEmployeeFormValidator.validate(employee, result);
    if (result.hasErrors()) {
      String[] roles = rolesService.getRoles();
      String employeeRole = employeesService.getEmployeeRole(id);

      if (employeeRole == null)
        return "redirect:/";

      model.addAttribute("roles", roles);
      model.addAttribute("employeeRole", employeeRole);
      return "/employee/edit";
    }
    employeesService.editEmployee(id, employee, role);
    return "redirect:/employee/list";
  }

}
