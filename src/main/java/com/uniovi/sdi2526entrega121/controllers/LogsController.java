package com.uniovi.sdi2526entrega121.controllers;

import com.uniovi.sdi2526entrega121.entities.LogEntry;
import com.uniovi.sdi2526entrega121.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador que gestiona la visualización y eliminación de logs del sistema.
 */
@Controller
public class LogsController {

  private final LoggerService loggerService;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param loggerService Servicio encargado de la gestión de logs.
   */
  @Autowired
  public LogsController(LoggerService loggerService) {
    this.loggerService = loggerService;
  }

  /**
   * Muestra la lista paginada completa de logs.
   *
   * @param model Modelo para añadir atributos a la vista.
   * @param pageable Información de paginación.
   * @return Vista con la lista completa de logs.
   */
  @RequestMapping(value="/log/list")
  public String listLogs(Model model, Pageable pageable) {
    Page<LogEntry> logList = loggerService.getAllLogs(pageable);
    model.addAttribute("logList", logList.getContent());
    model.addAttribute("page", logList);
    return "/log/list";
  }

  /**
   * Muestra una lista paginada de logs filtrados por tipo.
   *
   * @param logFilter Tipo de log por el que se filtra.
   * @param model Modelo para añadir atributos a la vista.
   * @param pageable Información de paginación.
   * @return Fragmento Thymeleaf con la tabla de logs filtrados.
   */
  @RequestMapping(value="/log/list/{logFilter}")
  public String listLogs(@PathVariable String logFilter, Model model, Pageable pageable) {
    Page<LogEntry> logList = loggerService.getLogsOfType(logFilter, pageable);
    model.addAttribute("logList", logList.getContent());
    model.addAttribute("page", logList);
    return "/fragments/logsTable";
  }

  /**
   * Elimina todos los logs de un tipo específico.
   *
   * @param logFilter Tipo de logs a eliminar.
   * @return Redirección a la lista completa de logs.
   */
  @RequestMapping("/log/delete/{logFilter}")
  public String deleteLogs(@PathVariable String logFilter) {
    loggerService.deleteLogsByType(logFilter);
    return "redirect:/log/list";
  }

}
