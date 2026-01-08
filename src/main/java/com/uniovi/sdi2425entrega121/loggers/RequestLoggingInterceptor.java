package com.uniovi.sdi2425entrega121.loggers;

import com.uniovi.sdi2425entrega121.entities.LogEntry;
import com.uniovi.sdi2425entrega121.services.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interceptor para registrar las peticiones HTTP entrantes, excepto recursos estáticos.
 * Registra detalles como  URI, parámetros de consulta y variables de ruta.
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  /** Rutas excluidas de logging (recursos estáticos) */
  private static final String[] EXCLUDED_PATHS = {"/css/", "/script/", "/images/"};

  /** Servicio para registrar logs personalizados */
  private final LoggerService loggerService;

  /**
   * Constructor que inyecta el servicio de logging.
   *
   * @param loggerService servicio para guardar logs en base de datos
   */
  public RequestLoggingInterceptor(LoggerService loggerService) {
    this.loggerService = loggerService;
  }

  /**
   * Ejecutado antes del procesamiento de la petición HTTP.
   * Filtra recursos estáticos y registra la información de la petición.
   *
   * @param request  la solicitud HTTP
   * @param response la respuesta HTTP
   * @param handler  el manejador para la petición (puede ser nulo)
   * @return true para continuar el procesamiento, false para abortar
   */
  @Override
  public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @Nullable Object handler) {
    String requestURI = request.getRequestURI();

    // Ignorar recursos estáticos
    if (isStaticResource(requestURI)) {
      return true;
    }

    // Obtener parámetros de consulta
    String queryParams = request.getQueryString() != null ? request.getQueryString() : "";

    // Obtener variables de ruta
    String pathVariables = getPathVariables(request);

    // Construir mensaje de log
    String logMessage = String.format(
            "PET - %s - [%s] %s - Params: %s %s",
            LocalDateTime.now(), request.getMethod(), request.getRequestURI(),
            queryParams, pathVariables
    );

    logger.info(logMessage);

    String logType = LogEntry.getLogType(0);
    loggerService.addLog(new LogEntry(logType, logMessage));
    return true;
  }

  /**
   * Obtiene las variables de ruta asociadas a la petición, si existen.
   *
   * @param request la solicitud HTTP
   * @return una cadena con las variables de ruta en formato clave=valor separadas por comas,
   *         o cadena vacía si no existen
   */
  @SuppressWarnings("unchecked") // Se asume que el atributo es Map<String, String>
  private String getPathVariables(HttpServletRequest request) {
    Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (attr instanceof Map<?, ?>) {
      Map<String, String> pathVariables = (Map<String, String>) attr;

      if (!pathVariables.isEmpty()) {
        return pathVariables.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
      }
    }
    return "";
  }

  /**
   * Comprueba si la URI solicitada corresponde a un recurso estático que no debe registrarse.
   *
   * @param requestURI la URI de la petición
   * @return true si es recurso estático excluido, false en caso contrario
   */
  private boolean isStaticResource(String requestURI) {
    for (String excludedPath : EXCLUDED_PATHS) {
      if (requestURI.startsWith(excludedPath)) {
        return true;
      }
    }
    return false;
  }
}
