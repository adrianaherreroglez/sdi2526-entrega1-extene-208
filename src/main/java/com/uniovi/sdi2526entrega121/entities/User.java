package com.uniovi.sdi2526entrega121.entities;

import javax.persistence.*;


/**
 * Representa a un usuario del sistema, asociado a un empleado,
 * con credenciales de acceso y un rol asignado.
 */

@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue
  private long id;
  @Column(unique = true)
  private String dni;

  private String password;
  @Transient // propiedad que no se almacena en la tabla.
  private String passwordConfirm;
  private String role;

  @OneToOne
  @JoinColumn(name = "empleado_id", nullable = false, unique = true)
  private Employee employee;

  public User() {}

  public User(String dni, Employee employee) {
    super();
    this.employee = employee;
    this.dni = dni;
    employee.setUser(this);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

}
