package com.uniovi.sdi2526entrega121.dtos;

/**
 * Clase cuya intención es crear objetos que pueda transportar los datos de la vista al controlador
 * para el cambio de contraseña en usuarios
 */
public class UserDTO {

  private String password;
  private String passwordConfirm;
  private String currentPassword;

  public UserDTO() {}

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  // Necesario que se usa indirectamente en otras partes, por eso da warning
  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }


  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }
}
