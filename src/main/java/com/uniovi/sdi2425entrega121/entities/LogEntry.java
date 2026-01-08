package com.uniovi.sdi2425entrega121.entities;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Representa una entrada de registro con tipo, fecha y descripción.
 * Se utiliza para guardar eventos importantes del sistema.
 */

@Entity
@Table(name = "logs")
public class LogEntry {

  private static final String[] logTypes = {"PET", "ALTA", "LOGIN-EX", "LOGIN-ERR", "LOGOUT"};

  @Id
  @GeneratedValue
  private Long id;

  private String logType;

  @Column(name = "fecha_hora")
  private LocalDateTime localDate; // Usamos LocalDateTime que se almacenará como TIMESTAMP en DB

  private String description;

  public LogEntry() {}

  public LogEntry(String logType, String description) {
    this.logType = logType;
    this.localDate = LocalDateTime.now();
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public static String getLogType(int position) {
    return logTypes[position];
  }

  public String getLogType() {
    return logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }

  public LocalDateTime getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDateTime localDate) {
    this.localDate = localDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
