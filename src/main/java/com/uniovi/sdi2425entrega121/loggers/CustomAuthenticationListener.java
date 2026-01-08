package com.uniovi.sdi2425entrega121.loggers;

import com.uniovi.sdi2425entrega121.entities.LogEntry;
import com.uniovi.sdi2425entrega121.services.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Listener personalizado que escucha eventos de autenticación en Spring Security
 * para registrar en el sistema los intentos exitosos y fallidos de login.
 */
@Component
public class CustomAuthenticationListener {

  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationListener.class);

  /** Servicio para guardar los logs en base de datos */
  private final LoggerService loggerService;

  /**
   * Constructor que inyecta el servicio de logging.
   *
   * @param loggerService servicio para gestionar logs
   */
  public CustomAuthenticationListener(LoggerService loggerService) {
    this.loggerService = loggerService;
  }

  /**
   * Evento que se ejecuta tras un login exitoso.
   * Registra el evento tanto en el logger de consola como en la base de datos.
   *
   * @param event evento de éxito en autenticación
   */
  @EventListener
  public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
    String dni = event.getAuthentication().getName();
    String logMessage = String.format("LOGIN-EX - %s - %s", LocalDateTime.now(), dni);

    logger.info(logMessage);

    String logType = LogEntry.getLogType(2);
    loggerService.addLog(new LogEntry(logType, logMessage));
  }

  /**
   * Evento que se ejecuta tras un intento fallido de login por credenciales incorrectas.
   * Registra el evento tanto en el logger de consola como en la base de datos.
   *
   * @param event evento de fallo en autenticación
   */
  @EventListener
  public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
    String dni = event.getAuthentication().getName();
    String logMessage = String.format("LOGIN-ERR - %s - %s", LocalDateTime.now(), dni);

    logger.info(logMessage);

    String logType = LogEntry.getLogType(3);
    loggerService.addLog(new LogEntry(logType, logMessage));
  }
}
