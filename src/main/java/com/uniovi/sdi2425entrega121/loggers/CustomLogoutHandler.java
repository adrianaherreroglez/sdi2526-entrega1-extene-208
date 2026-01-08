package com.uniovi.sdi2425entrega121.loggers;

import com.uniovi.sdi2425entrega121.services.LoggerService;
import com.uniovi.sdi2425entrega121.entities.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Manejador personalizado para gestionar eventos de cierre de sesión (logout).
 * Registra en el sistema los logs correspondientes cuando un usuario cierra sesión exitosamente.
 */
@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

  /** Servicio para gestionar el registro de logs en base de datos */
  private final LoggerService loggerService;

  /**
   * Constructor que recibe el servicio de logging.
   *
   * @param loggerService servicio para añadir logs a la base de datos
   */
  public CustomLogoutHandler(LoggerService loggerService) {
    this.loggerService = loggerService;
  }

  /**
   * Esto se ejecuta cuando el usuario cierra sesión con éxito.
   * Registra el evento tanto en el logger estándar como en la base de datos,
   * y redirige al usuario a la página de login con un parámetro indicando logout.
   *
   * @param request       petición HTTP
   * @param response      respuesta HTTP
   * @param authentication contexto de autenticación, puede ser null
   * @throws IOException si hay error al redirigir la respuesta
   */
  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication)
          throws IOException {
    String dni = (authentication != null) ? authentication.getName() : "Anonymous";
    String logMessage = String.format("LOGOUT - %s - %s", LocalDateTime.now(), dni);

    logger.info(logMessage);

    String logType = LogEntry.getLogType(4);
    loggerService.addLog(new LogEntry(logType, logMessage));

    // Redirige a la página de login con parámetro indicando cierre de sesión exitoso
    response.sendRedirect("/login?logout=true");
  }
}
