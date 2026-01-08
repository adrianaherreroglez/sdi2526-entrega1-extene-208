package com.uniovi.sdi2425entrega121.controllers;

import com.uniovi.sdi2425entrega121.dtos.UserDTO;
import com.uniovi.sdi2425entrega121.entities.User;
import com.uniovi.sdi2425entrega121.services.UsersService;
import com.uniovi.sdi2425entrega121.validators.ChangePasswordFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Controlador para gestionar las operaciones relacionadas con usuarios,
 * como login, cambio de contraseña y acceso restringido.
 */
@Controller
public class UsersController {

  private final UsersService usersService;
  private final ChangePasswordFormValidator changePasswordFormValidator;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param usersService Servicio para la gestión de usuarios.
   * @param changePasswordFormValidator Validador para el formulario de cambio de contraseña.
   */
  @Autowired
  public UsersController(UsersService usersService, ChangePasswordFormValidator changePasswordFormValidator) {
    this.usersService = usersService;
    this.changePasswordFormValidator = changePasswordFormValidator;
  }

  /**
   * Muestra la página de login. Si el usuario ya está autenticado, redirige a la página principal.
   * También muestra mensajes de error o logout si se reciben en la petición.
   *
   * @param error Parámetro que indica error de autenticación (opcional).
   * @param logout Parámetro que indica que el usuario ha cerrado sesión (opcional).
   * @param model Modelo para pasar datos a la vista.
   * @param principal Usuario autenticado (si existe).
   * @return Vista de login o redirección a la página principal.
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "logout", required = false) String logout, Model model,
                      Principal principal) {
    if (principal != null) { // Si el usuario ya está autenticado
      return "redirect:/";
    }
    if (logout != null) {
      model.addAttribute("logout", logout);
    }
    if (error != null) {
      model.addAttribute("error", error);
    }
    return "login";
  }

  /**
   * Muestra la página principal del sistema tras el login.
   *
   * @return Vista principal.
   */
  @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
  public String home() {
    return "home";
  }

  /**
   * Muestra el formulario para cambiar la contraseña del usuario autenticado.
   *
   * @param model Modelo para pasar datos a la vista.
   * @return Vista con el formulario de cambio de contraseña.
   */
  @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
  public String changePassword(Model model) {
    model.addAttribute("userDTO", new UserDTO());
    return "/changePassword";
  }

  /**
   * Procesa el formulario de cambio de contraseña, validando los datos y actualizando la contraseña
   * del usuario autenticado si está correcto.
   *
   * @param userDTO DTO que contiene la nueva contraseña y confirmación.
   * @param result Resultado de la validación del formulario.
   * @param principal Usuario autenticado.
   * @return Redirección a la página principal o recarga del formulario si hay errores.
   */
  @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
  public String changePassword(@Validated UserDTO userDTO, BindingResult result,
                               Principal principal) {
    String userDni = principal.getName();
    changePasswordFormValidator.validate(userDni, userDTO, result);
    if (result.hasErrors()) {
      return "/changePassword";
    }
    User currentUser = usersService.getUserByDni(userDni);
    usersService.changePassword(currentUser.getId(), userDTO.getPassword());
    return "home";
  }

  /**
   * Muestra la página de acceso denegado cuando un usuario intenta acceder a una zona restringida.
   *
   * @return Vista de acceso denegado.
   */
  @RequestMapping(value="/accessDenied")
  public String accessDenied() {
    return "/accessDenied";
  }
}
