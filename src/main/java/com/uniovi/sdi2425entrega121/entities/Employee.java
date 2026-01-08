package com.uniovi.sdi2425entrega121.entities;

import javax.persistence.*;

/**
 * Representa a un empleado con su DNI, nombre, apellidos
 * y la relación con su usuario del sistema.
 */


@Entity
public class Employee {

  @Id
  @GeneratedValue
  private Long id;
  @Column(unique = true)
  private String dni;
  private String name;
  private String surname;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private User user;



  public Employee() {
  }

  public Employee(String dni, String name, String surname) {
    if (dni == null || dni.isEmpty()) {
      throw new IllegalArgumentException("dni cannot be null or empty");
    }
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("name cannot be null or empty");
    }
    if (surname == null || surname.isEmpty()) {
      throw new IllegalArgumentException("surname cannot be null or empty");
    }

    this.dni = dni;
    this.name = name;
    this.surname = surname;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
