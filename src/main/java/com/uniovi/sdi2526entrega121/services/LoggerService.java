package com.uniovi.sdi2526entrega121.services;

import com.uniovi.sdi2526entrega121.entities.LogEntry;
import com.uniovi.sdi2526entrega121.repositories.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoggerService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final LoggerRepository loggerRepository;


  public LoggerService(LoggerRepository loggerRepository) {
    this.loggerRepository = loggerRepository;
  }


  public void addLog(LogEntry logEntry) {
    loggerRepository.save(logEntry);
  }

  public Page<LogEntry> getAllLogs(Pageable pageable) {
    return loggerRepository.findAll(pageable);
  }

  public LogEntry getLog(Long id) {
    return loggerRepository.findById(id).isPresent() ? loggerRepository.findById(id).get() : null;
  }

  public void saveAddEmployeeLog(HttpServletRequest request) {
    String url = request.getRequestURL().toString();
    String method = request.getMethod();
    String params = getRequestParams(request);

    // Crear mensaje de log
    String logMessage = String.format("ALTA - %s - [%s] %s - Params: %s",
        LocalDateTime.now(), method, url, params);

    // Guardar log en consola y en la base de datos
    logger.info(logMessage);

    String logType = LogEntry.getLogType(1);
    loggerRepository.save(new LogEntry(logType, logMessage));
  }

  /**
   * Función auxiliar para obtener los parámetros recibidos en una petición
   * @return parámetros recibidos
   */
  private String getRequestParams(HttpServletRequest request) {
    Map<String, String[]> paramMap = request.getParameterMap();
    if (paramMap.isEmpty()) {
      return "Ninguno";
    }
    return paramMap.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
        .collect(Collectors.joining(", "));
  }

  public Page<LogEntry> getLogsOfType(String logFilter, Pageable pageable) {
    if (logFilter.equals("ALL"))
        return getAllLogs(pageable);
    else
      return loggerRepository.findLogByType(logFilter, pageable);
  }

  public void deleteLogsByType(String logFilter) {
    if ("ALL".equals(logFilter)) {
      loggerRepository.deleteAll();
    } else {
      loggerRepository.deleteLogsByType(logFilter);
    }
  }

  public int getNumberOfLogsByType(String logType) {
    return loggerRepository.countLogEntriesByLogType(logType);
  }
}
