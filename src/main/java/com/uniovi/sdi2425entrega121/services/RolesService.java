package com.uniovi.sdi2425entrega121.services;

import org.springframework.stereotype.Service;


/**
 * Servicio para gestionar los roles disponibles en la aplicación.
 */
@Service
public class RolesService {

  /**
   * Array con los roles definidos en la aplicación.
   */
  private final String[] roles = {"ROLE_STANDARD", "ROLE_ADMIN"};

  /**
   * Devuelve los roles disponibles en la aplicación.
   *
   * @return array con los nombres de los roles
   */
  public String[] getRoles() {
    return roles;
  }
}
